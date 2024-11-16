package com.seven.delivr.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cac-api", url = "${CAC_URL}")
public interface VendorVerificationProxy {
    @PostMapping
    public CACResponse queryCAC(@RequestBody VendorVerificationProxyRequest request);
}
