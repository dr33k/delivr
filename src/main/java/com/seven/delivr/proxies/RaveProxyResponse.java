package com.seven.delivr.proxies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RaveProxyResponse {
    public RaveProxyResponse(){}
    public static class RaveResponse{
        public String status;
        public String message;
        public RaveData data;
        public RaveResponse(){}
    }
    public static class RaveSubaccountResponse{
        public String status;
        public String message;
        public RaveSubaccountData data;

        public RaveSubaccountResponse(){}
    }
    public static class RaveBankListResponse{
        public String status;
        public String message;
        public List<Bank> data;
        public RaveBankListResponse(){}
    }

    public static class RaveData{
        public String status;
        public String link;
        @JsonProperty(value = "account_name")
        public String accountName;
        public RaveData(){}
    }

    public static class Bank{
        public String code;
        public String name;
        public Bank(){}
    }
    public static class RaveSubaccountData{
        @JsonProperty(value = "subaccount_id")
        public String subaccountId;
        @JsonProperty(value = "bank_name")
        public String bankName;
        public RaveSubaccountData(){}
    }
}
