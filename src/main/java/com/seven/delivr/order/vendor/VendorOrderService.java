package com.seven.delivr.order.vendor;

import com.seven.delivr.base.AppService;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.customer.CustomerOrder;
import com.seven.delivr.order.customer.CustomerOrderRepository;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.rider.Rider;
import com.seven.delivr.rider.RiderService;
import com.seven.delivr.user.User;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.seven.delivr.enums.PublicEnum.OrderStatus.*;

@Service
public class VendorOrderService implements AppService {
    private final VendorOrderRepository vendorOrderRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final RiderService riderService;
    private User principal;
    private final EntityManager em;


    public VendorOrderService(VendorOrderRepository vendorOrderRepository,
                              CustomerOrderRepository customerOrderRepository,
                              User principal,
                              EntityManager entityManager,
                              RiderService riderService){
        this.vendorOrderRepository = vendorOrderRepository;
        this.principal = principal;
        this.em = entityManager;
        this.customerOrderRepository = customerOrderRepository;
        this.riderService = riderService;
    }

    public  List<VendorOrderMinifiedRecord> getAll(AppPageRequest request) {
        return vendorOrderRepository.findAllByVendor(em.getReference(Vendor.class, principal.getVendor().getId()), PageRequest.of(
                request.getOffset(),
                request.getLimit(),
                Sort.by(Sort.Direction.DESC, "dateCreated"))).stream().map(VendorOrderMinifiedRecord::map).toList();
    }

    public List<VendorOrderMinifiedRecord> filterForVendor(PublicEnum.OrderStatus status, ZonedDateTime dateCreated, AppPageRequest request){
        if(status == null) return getAll(request);

        return vendorOrderRepository.findAllByStatusAndVendorAndDateCreated(status, principal.getVendor(), dateCreated, PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(VendorOrderMinifiedRecord::map).toList();
    }
    public List<VendorOrderMinifiedRecord> filterForCustomer(PublicEnum.OrderStatus status, ZonedDateTime dateCreated,  AppPageRequest request){
        if(dateCreated == null)
            return vendorOrderRepository.findAllByStatusAndUser(status, em.getReference(User.class, principal.getId()), PageRequest.of(
                            request.getOffset(),
                            request.getLimit(),
                            Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(VendorOrderMinifiedRecord::map).toList();

        return vendorOrderRepository.findAllByStatusAndUserAndDateCreated(status, em.getReference(User.class, principal.getId()), dateCreated,  PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(VendorOrderMinifiedRecord::map).toList();
    }

    public VendorOrderDetailedRecord get(UUID id) {
        return VendorOrderDetailedRecord.map(vendorOrderRepository.findByIdAndVendor(id, principal.getVendor()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Transactional
    public VendorOrderDetailedRecord patch(UUID id, PublicEnum.OrderStatus status) {
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(id, principal.getVendor()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!vendorOrder.getIsPaid()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Order has not been paid for");

//        // PAID -> ACCEPTED Notifies Rider
//        if(vendorOrder.getStatus().equals(PublicEnum.OrderStatus.PAID) &&  status == PublicEnum.OrderStatus.ACCEPTED) {
//            vendorOrder.setStatus(status);
//            CustomerOrder customerOrder = vendorOrder.getCustomerOrder();
//
//            //Make sure all orders have been accepted before alerting riders
//            List<VendorOrder> notAccepted = customerOrder.getVendorOrders().stream().filter(
//                    vo -> !vo.getStatus().equals(PublicEnum.OrderStatus.ACCEPTED)).toList();
//
//            if(notAccepted.isEmpty())
//                riderService.broadcastToRiders(customerOrder, null);
//
//            customerOrderRepository.saveAndFlush(customerOrder);
//            return VendorOrderDetailedRecord.map(vendorOrder);
//        }
        //PAID -> READY
            // Notifies Rider when at least 50% of the total number of vendor orders is ready
        //Notifies Customer when each vendor order is ready
        else if(vendorOrder.getStatus().equals(PublicEnum.OrderStatus.PAID)  && status == PublicEnum.OrderStatus.READY) {
            CustomerOrder customerOrder = setReady(vendorOrder);

            //Make sure at least 50% of orders are ready before alerting riders
            if((float)(customerOrder.getNoOfReadyVendorOrders() / customerOrder.getNoOfVendorOrders()) >= 0.5 ) {
                List<Rider> riders = riderService.getAll();
                riderService.broadcastToRiders(customerOrder, null, riders);
            }

            customerOrderRepository.saveAndFlush(customerOrder);
            return VendorOrderDetailedRecord.map(vendorOrder);
        }
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Operation not allowed. Current value: %s", vendorOrder.getStatus().toString()));
    }

    private synchronized CustomerOrder setReady(VendorOrder vendorOrder){
        vendorOrder.setStatus(READY);
        CustomerOrder customerOrder = vendorOrder.getCustomerOrder();
        customerOrder.setNoOfReadyVendorOrders(customerOrder.getNoOfReadyVendorOrders() + 1);
        return customerOrder;
    }

    public void delete(UUID id) {vendorOrderRepository.deleteByIdAndVendor(id, principal.getVendor());}
}
