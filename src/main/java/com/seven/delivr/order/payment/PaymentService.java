package com.seven.delivr.order.payment;

import com.seven.delivr.base.AppService;
import com.seven.delivr.notification.NotificationService;
import com.seven.delivr.order.customer.CustomerOrder;
import com.seven.delivr.order.customer.CustomerOrderRepository;
import com.seven.delivr.order.customer.cart.CartItemService;
import com.seven.delivr.order.vendor.VendorOrder;
import com.seven.delivr.order.vendor.VendorOrderRepository;
import com.seven.delivr.proxies.FlutterwaveProxy;
import com.seven.delivr.proxies.RaveProxyResponse;
import com.seven.delivr.user.User;
import org.json.JSONArray;
import org.json.JSONObject;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService implements AppService {
    private final PaymentRepository paymentRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final User principal;
    private final FlutterwaveProxy flutterwaveProxy;
    private final Environment environment;
    private final CartItemService cartItemService;
    private final EntityManager em;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepository,
                          CustomerOrderRepository customerOrderRepository,
                          User principal,
                          FlutterwaveProxy flutterwaveProxy,
                          VendorOrderRepository vendorOrderRepository,
                          Environment environment,
                          CartItemService cartItemService,
                          EntityManager em,
                          NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.principal = principal;
        this.flutterwaveProxy = flutterwaveProxy;
        this.environment = environment;
        this.cartItemService = cartItemService;
        this.em = em;
        this.notificationService = notificationService;
    }
    //Step 1: Initiate Payment
    @Transactional
    public String initiatePayment(PaymentRequest request) {
        CustomerOrder customerOrder = customerOrderRepository.findByIdAndUser(request.customerOrderId, em.getReference(User.class, principal.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (customerOrder.getIsPaid())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already made");

        Payment payment = createPaymentObject(customerOrder);

        JSONObject paymentRequest = new JSONObject();

        JSONArray subaccounts = new JSONArray();

        final double totalCost = customerOrder.getTotalCost();
        double serviceFee = customerOrder.getServiceFee();
        customerOrder.getVendorOrders().forEach(
                vendorOrder -> {
                    double ratio = vendorOrder.getTotalCost() /totalCost;
                    subaccounts.put(
                    new JSONObject().put("transaction_charge_type", "flat")
                                    .put("transaction_charge", (ratio * serviceFee))
                                    .put("id", vendorOrder.getVendorAccount().getSubaccountCode())
                                    .put("transaction_split_ratio", ratio * 10)
                    );
                });

        paymentRequest.put("subaccounts", subaccounts);

        User customer = customerOrder.getUser();

        paymentRequest.put("customer", new JSONObject()
                .put("name", customer.getFname() + " " + customer.getLname())
                .put("phonenumber", customer.getPhoneNo())
                .put("email", customer.getEmail()));

        paymentRequest.put("customizations", new JSONObject()
                .put("title", "Rooks Checkout")
                .put("description", "Last step"));

        paymentRequest.put("amount", String.valueOf(totalCost));
        paymentRequest.put("currency", customerOrder.getCurrency().toString());
        paymentRequest.put("tx_ref", payment.getTrefUuid().toString());
        //.put("redirect_url", String.format("https://rooks-api-68da9ef85a5b.herokuapp.com/%s/payment/callback", Constants.VERSION));
        paymentRequest.put("redirect_url", String.format("%s/%s/payment/callback", environment.getProperty("ROOKS_API_DOMAIN"), Constants.VERSION));

        System.out.println(paymentRequest.toMap().toString());
        RaveProxyResponse.RaveResponse response = flutterwaveProxy.createPayment("Bearer " + environment.getProperty("FLUTTERWAVE_SECRET_KEY"), paymentRequest.toMap());

        if (!response.status.equals("success"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.message);

        paymentRepository.save(payment);
        return response.data.link;
    }
    //Step 3: Validate Payment
    @Transactional
    public Boolean validatePayment(UUID tRefUUID, Integer paymentProcessorTxId) {
        Payment payment = paymentRepository.findByTrefUuid(tRefUUID).get();

        RaveProxyResponse.RaveResponse response;

        if (paymentProcessorTxId != null) {
            payment.setPaymentProcessorTxId(paymentProcessorTxId);
            response = flutterwaveProxy.verifyTransaction("Bearer " + environment.getProperty("FLUTTERWAVE_SECRET_KEY"), payment.getPaymentProcessorTxId());
            paymentRepository.save(payment);
        } else
            response = flutterwaveProxy.verifyTransaction("Bearer " + environment.getProperty("FLUTTERWAVE_SECRET_KEY"), tRefUUID);

        if (!payment.getIsPaid() && (response.data.status.equals("success") || response.data.status.equals("successful")))
            paymentSuccess(payment);
        return payment.getIsPaid();
    }

    private void paymentSuccess(Payment payment) {
        payment.setIsPaid(Boolean.TRUE);

        CustomerOrder customerOrder = payment.getCustomerOrder();
        customerOrder.setIsPaid(Boolean.TRUE);

        List<VendorOrder> vendorOrders = customerOrder.getVendorOrders();
        vendorOrders.forEach(vendorOrder -> {
            vendorOrder.setStatus(PublicEnum.OrderStatus.PAID);
            vendorOrder.setIsPaid(Boolean.TRUE);
        });

        customerOrderRepository.save(customerOrder);
        paymentRepository.save(payment);
        cartItemService.clearCart(customerOrder.getUser().getId());
    }

    private Payment createPaymentObject(CustomerOrder customerOrder) {
        return Payment.builder()
                .customerOrder(customerOrder)
                .amount(customerOrder.getTotalCost())
                .currency(customerOrder.getCurrency())
                .isPaid(Boolean.FALSE)
                .trefUuid(UUID.randomUUID())
                .paymentProcessor(PublicEnum.PaymentProcessor.FLUTTERWAVE)
                .build();
    }

//    private RaveProxyResponse.RaveResponse createCharge(JSONObject chargeRequest){
//        String encryptedString = new String(keyService.encrypt3DES(chargeRequest.toString()),  StandardCharsets.UTF_8);
//        System.out.println(encryptedString);
//        RaveProxyResponse.RaveResponse response = flutterwaveProxy.createCharge("Bearer "+environment.getProperty("FLUTTERWAVE_SECRET_KEY"), "card", new RaveProxyRequest.ChargeRequestEncrypted(encryptedString));
//
//        if(!response.status.equals("success"))
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not process payment. Reason: " + response.message);
//        if (!response.data.meta.authorization.mode.equals("redirect")) {
//            System.out.println("MODE: "+response.data.meta.authorization.mode);
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not supported. Please try a different card");
//        }
//        return response;
//    }

}
