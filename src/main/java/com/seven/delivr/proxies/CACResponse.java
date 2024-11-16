package com.seven.delivr.proxies;

import java.util.List;

public class CACResponse {
    public String status;
    public List<CACResponseData> data;
    public CACResponse(){}

    public static class CACResponseData{
        public String address;
        public String approvedName;
        public String rcNumber;
        public String email;

        public CACResponseData(){}
    }

}

