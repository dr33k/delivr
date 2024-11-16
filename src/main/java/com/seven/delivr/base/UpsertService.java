package com.seven.delivr.base;

import com.seven.delivr.requests.AppRequest;

public interface UpsertService<REQ extends AppRequest, ID>{
    <RES> RES get(ID id);
    <RES> RES upsert(REQ request);
    void delete(ID id);
}
