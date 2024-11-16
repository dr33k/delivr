package com.seven.delivr.order.customer;

import com.seven.delivr.base.CUService;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.customer.cart.CartItemRecord;
import com.seven.delivr.order.customer.cart.CartItemService;
import com.seven.delivr.order.item.OrderItem;
import com.seven.delivr.order.vendor.VendorOrder;
import com.seven.delivr.product.Product;
import com.seven.delivr.product.ProductRepository;
import com.seven.delivr.user.User;
import com.seven.delivr.user.UserRepository;
import com.seven.delivr.user.location.Location;
import com.seven.delivr.vendor.Vendor;
import com.seven.delivr.vendor.VendorRepository;
import com.seven.delivr.vendor.account.VendorAccountService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static com.seven.delivr.enums.PublicEnum.OrderStatus.*;

@Service
public class CustomerOrderService implements CUService<CustomerOrderCreateRequest, CustomerOrderPatchRequest, UUID> {
    private final ProductRepository productRepository;
    private final User principal;
    private final UserRepository userRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final EntityManager em;
    private final VendorRepository vendorRepository;
    private final VendorAccountService vendorAccountService;
    private final CartItemService cartItemService;

    public CustomerOrderService(ProductRepository productRepository,
                                User principal,
                                UserRepository userRepository,
                                CustomerOrderRepository customerOrderRepository,
                                EntityManager entityManager,
                                VendorRepository vendorRepository,
                                VendorAccountService vendorAccountService,
                                CartItemService cartItemService) {
        this.productRepository = productRepository;
        this.principal = principal;
        this.userRepository = userRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.em = entityManager;
        this.vendorAccountService = vendorAccountService;
        this.vendorRepository = vendorRepository;
        this.cartItemService = cartItemService;
    }

    public List<CustomerOrderMinifiedRecord> getAll(FilterPageRequest request) {
        return customerOrderRepository.findAllByUserAndIsPaid(em.getReference(User.class, principal.getId()), request.getIsPaid(), PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(CustomerOrderMinifiedRecord::map).toList();
    }
    @Override
    public CustomerOrderRecord get(UUID id) {
        return CustomerOrderRecord.map(customerOrderRepository.findByIdAndUser(id, em.getReference(User.class, principal.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional
    public CustomerOrderRecord create(CustomerOrderCreateRequest request){
        final Double PRICE_LIMIT = 0.0;
        //Create CustomerOrder
        CustomerOrder customerOrder = createCustomerOrder(request);

        //Map Vendor Ids to OrderItems from the User's Cart
        Map<Long, List<OrderItem>> vendorIdToOrderItemMap = getVendorIdToOrderItemMap();
        if(vendorIdToOrderItemMap.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Empty Cart");

        List<VendorOrder> vendorOrders = vendorIdToOrderItemMap.entrySet().stream().map(entry ->{
                    Vendor vendor = vendorRepository.findById(entry.getKey()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor with id "+entry.getKey()+" not found"));

                    int eta = entry.getValue().stream().map(oi-> calculateEta(oi.getProduct())).reduce(Integer::max).orElse(0);

                    VendorOrder vendorOrder = VendorOrder.builder().vendor(vendor)
                            .vendorOrderNo(UUID.randomUUID())
                            .customerOrder(customerOrder)
                            .status(CREATED)
                            .isPaid(Boolean.FALSE)
                            .orderItems(entry.getValue())
                            .eta(eta)
                            .totalCost(entry.getValue().stream().mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getUnits()).sum())
                            .currency(entry.getValue().get(0).getCurrency())
                            .vendorAccount(vendorAccountService.getPrimaryVendorAccount(vendor))
                            .build();

                    entry.getValue().forEach(orderItem -> orderItem.setVendorOrder(vendorOrder));
                    return vendorOrder;
                }
               ).toList();

        customerOrder.setVendorOrders(vendorOrders);
        customerOrder.setNoOfVendorOrders(vendorOrders.size());

        double totalCost = vendorOrders.stream().map(VendorOrder::getTotalCost).reduce(Double::sum).orElse(0.0);

        if(totalCost < 1000)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order total falls below limit of NGN 1,000.00");

        customerOrder.setServiceFee(calculateServiceFee(totalCost));
        customerOrder.setDeliveryFee(0.0);

        totalCost = calculateStampDuty(totalCost);

        customerOrder.setTotalCost(totalCost);
        customerOrder.setCurrency(PublicEnum.Currency.NGN);
        customerOrder.setIsPaid(Boolean.FALSE);

        return CustomerOrderRecord.map(customerOrderRepository.save(customerOrder));
    }

    private Map<Long, List<OrderItem>> getVendorIdToOrderItemMap() {
        Map<Long, List<CartItemRecord>> cartItems = cartItemService.getAll();
        Map<Long, List<OrderItem>> vendorIdToOrderItemMap = new HashMap<>();

        cartItems.forEach((vendorId, cartItemList) -> {
            Map<UUID, Product> productNoToProductMap = productRepository.findAllByProductNoIn(getProductNos(cartItemList)).stream().collect(Collectors.toMap(Product::getProductNo, product -> product));

            List<OrderItem> orderItems = cartItemList.stream()
                    .map(cartItem -> {
                        Product p = productNoToProductMap.get(cartItem.productItem().productNo());
                        if(!p.getIsReady())
                            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("%s is not in stock right now, it'll be ready in less than %d minutes", p.getTitle(), p.getPreparationTimeMins()));
                        if(!p.getIsPublished())
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "This item is currently unavailable");


                        OrderItem oi = OrderItem.builder()
                                .product(p)
                                .units(cartItem.units())
                                .note(cartItem.note())
                                .price(p.getPrice())
                                .currency(p.getCurrency()).build();
                        return oi;
                    }).toList();

            vendorIdToOrderItemMap.put(vendorId, orderItems);
        });
        return vendorIdToOrderItemMap;
    }

    private List<UUID> getProductNos(List<CartItemRecord> cartItemList) {
        return cartItemList.stream().map(cartItemRecord -> cartItemRecord.productItem().productNo()).toList();
    }

    private CustomerOrder createCustomerOrder(CustomerOrderCreateRequest request) {
        User user = userRepository.findById(principal.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Location deliveryLocation = user.getLocations().stream().filter(userLocation -> Objects.equals(userLocation.getId(), request.customerLocationId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Delivery location selected is not among user's registered addresses"));

        //Create CustomerOrder with user details, delivery location
        return new CustomerOrder(user, deliveryLocation);
    }
    private int calculateEta(Product product){
        return product.getPreparationTimeMins() + product.getDeliveryTimeMins();
    }
    private double calculateStampDuty(double total) {
        return total >= 10000 ? total + 50 : total;
    }
    private double calculateServiceFee(double total){
        final double MIN_TOTAL = 100;
        final double MAX_SERVICE_FEE = 1000;

        double serviceFee = Math.max((0.02 * total), MIN_TOTAL);
        return Math.min(MAX_SERVICE_FEE, serviceFee);
    }

    @Override
    @Transactional
    public CustomerOrderMinifiedRecord update(UUID id, CustomerOrderPatchRequest request) {return null;}

    @Override
    @Transactional
    public void delete(UUID id) {
        customerOrderRepository.deleteByIdAndUser(id, em.getReference(User.class, principal.getId()));
    }
}
