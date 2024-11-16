package com.seven.delivr.requests;

public class AppPageRequest implements AppRequest{

    protected int limit;
    protected int offset;

    public AppPageRequest(){
        this.limit = 10;
        this.offset = 0;
    }

    public AppPageRequest(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
