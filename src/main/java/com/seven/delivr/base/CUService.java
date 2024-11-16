package com.seven.delivr.base;

import com.seven.delivr.requests.AppRequest;

public interface CUService<CREQ extends AppRequest,UREQ extends AppRequest, ID>{

    <RES> RES get(ID id);
    <RES> RES create(CREQ request);
    <RES> RES update(ID id,UREQ request);
    void delete(ID id);
}
