package com.seven.delivr.order.customer;

public class AlreadyDeliveredException extends RuntimeException{
    public AlreadyDeliveredException(String message){
        super(message);
    }
}
