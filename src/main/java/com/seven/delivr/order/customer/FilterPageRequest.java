package com.seven.delivr.order.customer;

import com.seven.delivr.requests.AppPageRequest;

public class FilterPageRequest extends AppPageRequest {
    private Boolean isPaid;

    public FilterPageRequest(){
        this.limit = 10;
        this.offset = 0;
        this.isPaid = Boolean.TRUE;
    }
    public FilterPageRequest(int limit, int offset, boolean isPaid){
        this.limit = limit;
        this.offset = offset;
        this.isPaid = isPaid;
    }
    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean paid) {
        isPaid = paid;
    }
}
