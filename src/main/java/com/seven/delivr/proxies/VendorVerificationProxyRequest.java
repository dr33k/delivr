package com.seven.delivr.proxies;

import com.seven.delivr.requests.AppRequest;

public class VendorVerificationProxyRequest implements AppRequest {
    public String searchTerm;
    public VendorVerificationProxyRequest(String incorporationNo){
        this.searchTerm = incorporationNo;
    }
}
