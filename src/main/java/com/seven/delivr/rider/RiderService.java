package com.seven.delivr.rider;

import com.seven.delivr.base.UpsertService;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.notification.NotificationService;
import com.seven.delivr.order.customer.AlreadyDeliveredException;
import com.seven.delivr.order.customer.CustomerOrder;
import com.seven.delivr.order.customer.CustomerOrderRepository;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.util.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.seven.delivr.enums.PublicEnum.OrderStatus.*;
import static com.seven.delivr.enums.PublicEnum.OrderStatus.DELIVERED;

@Service
public class RiderService implements UpsertService<RiderUpsertRequest, UUID> {
    private final RiderRepository riderRepository;
    private final NotificationService notificationService;
    private final CustomerOrderRepository customerOrderRepository;
    @Value("delivr.api.domain")
    private String rooksApiDomain;

    public RiderService(RiderRepository riderRepository,
                        NotificationService notificationService,
                        CustomerOrderRepository customerOrderRepository) {
        this.riderRepository = riderRepository;
        this.notificationService = notificationService;
        this.customerOrderRepository = customerOrderRepository;
    }

    public <P extends AppPageRequest> List<Rider> getAll(P request) {return riderRepository.findAll( PageRequest.of(
                    request.getOffset(),
                    request.getLimit(),
                    Sort.by(Sort.Direction.DESC, "dateCreated"))).toList();
    }
    public List<Rider> getAll() {return riderRepository.findAll();}

    @Override
    public <RES> RES get(UUID uuid) {
        return null;
    }

    @Override
    public Rider upsert(RiderUpsertRequest request) {
        if(request.id == null){
            Rider rider = Rider.creationMap(request);
            return riderRepository.saveAndFlush(rider);
        }
        else{
            Rider rider = riderRepository.findById(request.id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
            Rider.updateMap(request, rider);
            return riderRepository.save(rider);
        }
    }

    @Override
    public void delete(UUID id) {riderRepository.deleteById(id);}

    public void textRider(UUID riderId) {
//        Rider rider = riderRepository.findById(riderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        notificationService.sendSMS("NoReply: Hello " + rider.getName(), List.of(rider.getPhoneNo()));
    }
    public void broadcastToRiders(CustomerOrder customerOrder, UUID customerOrderId, List<Rider> riders) {
//        Map<String, String> phoneNoToMessageBodyMap = null;
//
//        if (customerOrder != null) {
//            phoneNoToMessageBodyMap = convertOrderToBroadcastMessage(riders, customerOrder);
//        } else if (customerOrderId != null) {
//            phoneNoToMessageBodyMap = convertOrderToBroadcastMessage(riders,
//                    customerOrderRepository.findById(customerOrderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
//        } else throw new ResponseStatusException(HttpStatus.CONFLICT, "Null customer order details");
//
//        notificationService.sendSMS(phoneNoToMessageBodyMap);
    }

    @Transactional
    public synchronized String acceptOrderDelivery(UUID id, UUID customerOrderId) {
//        //SEARCH FOR ORDER
//        CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order details unavailable"));
//        //TERMINATE IF ORDER HAS ALREADY BEEN ACCEPTED
//        Rider assignedRider = customerOrder.getRider();
//        if (assignedRider != null){
//            if(assignedRider.getId().equals(id)){
//                String message = String.format("Hello %s, you were assigned this order: %s.\n", assignedRider.getName(), customerOrderId);
//
//                if(!customerOrder.getVendorOrders().stream().findAny().get().getStatus().equals(DELIVERED)){
//                    message += String.format("However, it has NOT BEEN DELIVERED yet to customer: %s ." +
//                            "Are you done but forgot to mark it as delivered ?" +
//                            "You can do so on the ORDER ASSIGNMENT SMS sent to you", customerOrder.getUser().getFullName());
//                }
//                else message += String.format("It has been DELIVERED SUCCESSFULLY to customer: %s. Thank you", customerOrder.getUser().getFullName());
//             return message;
//            }
//            return "This order was just accepted by another Rooks Rider.\n" +
//                    "Please try accepting another one or stay updated for newer one";
//        }
//
//        //ASSIGN ORDER TO RIDER
//        Rider rider = riderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rider details not found"));
//        customerOrder.setRider(rider);
//        customerOrderRepository.saveAndFlush(customerOrder);
//
//        //SEND CONFIRMATION MESSAGE TO RIDER
//        String messageBody = convertOrderToAssignMessage(rider, customerOrder);
//        notificationService.sendSMS(messageBody, List.of(rider.getPhoneNo()));
//
//        return String.format(
//                "Order: %s accepted by %s.\n" +
//                        "Please wait for an SMS to be sent to you with details about the order",
//                customerOrder.getId(),
//                rider.getName());

    return null;
    }
    @Transactional
    public String markDelivered(UUID id, PublicEnum.OrderStatus status) {
//        CustomerOrder customerOrder = customerOrderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This order record was not found"));
//        try {
//            customerOrder.getVendorOrders().forEach(vendorOrder -> {
//                PublicEnum.OrderStatus currentStatus = vendorOrder.getStatus();
//
//                if (currentStatus.equals(DELIVERED))
//                    throw new AlreadyDeliveredException("This order has already been marked as DELIVERED.\nOrder: " + customerOrder.getId());
//
//                if ((currentStatus.equals(PAID) || currentStatus.equals(ACCEPTED) || currentStatus.equals(READY)) && status.equals(DELIVERED)) {
//                    vendorOrder.setStatus(status);
//                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Operation not allowed by user");
//            });
//            customerOrderRepository.saveAndFlush(customerOrder);
//            return "Order " + customerOrder.getId() + " is now DELIVERED";
//        } catch (Exception e) {
//            return e.getMessage();
//        }
        return null;
    }

    private String convertOrderToAssignMessage(Rider rider, CustomerOrder customerOrder) {
//        String pickupLocations = customerOrder.getVendorOrders().stream().map(vo -> vo.getVendor().getNameAndLocation()).collect(Collectors.joining("\n"));
//
//        return String.format("""
//                        ORDER ASSIGNMENT!: ROOKS
//
//                        ORDER NO: %s
//
//                        CUSTOMER: %s
//                        CUSTOMER PHONE: %s
//
//                        Hello %s, you've got a new delivery.
//
//                        PICK UPS:
//                        %s
//
//                        DROP-OFF LOCATION:
//                        %s
//
//                        Follow this link to mark the order as DELIVERED after drop-off
//                        %s
//                        """,
//                customerOrder.getId().toString(),
//                customerOrder.getUser().getFullName(),
//                customerOrder.getUser().getPhoneNo(),
//                rider.getName(),
//                pickupLocations,
//                customerOrder.getLocation().toString(),
//                String.format("%s/%s/rider/%s/c_order/%s/delivered", rooksApiDomain, Constants.VERSION, rider.getId(), customerOrder.getId().toString())
//        );
        return null;
    }

    private Map<String, String> convertOrderToBroadcastMessage(List<Rider> riders, CustomerOrder customerOrder) {
//        Map<String, String> phoneNoToMessageBodyMap = new HashMap<>();
//
//        riders.forEach(rider ->
//                phoneNoToMessageBodyMap.put(rider.getPhoneNo(),
//                        String.format("""
//                                        ROOKS: NEW DELIVERY ALERT!
//                                        DROP-OFF LOCATION:
//                                        %s
//                                        Follow the link below to accept the order:
//                                        %s
//                                        """,
//                                customerOrder.getLocation().toString(),
//                                String.format("%s/%s/c_order/%s/accept/%s", rooksApiDomain, Constants.VERSION, customerOrder.getId().toString(), rider.getId())
//                        )));
//        return phoneNoToMessageBodyMap;
    return null;
    }

//    @Scheduled(fixedRate = 120000L)
//    //Rebroadcasts every 2 minutes for undelivered READY orders that are less than a week old
//    public void rebroadcastForStarvedOrders(){
//        List<CustomerOrder> starvedOrders = customerOrderRepository.getStarvedOrders(ZonedDateTime.now().minusWeeks(1));
//
//        if(!starvedOrders.isEmpty()){
//            List<Rider>riders = riderRepository.findAll();
//            starvedOrders.forEach(so->{
//                broadcastToRiders(so, null, riders);
//            });
//        }
//    }
}
