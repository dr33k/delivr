package com.seven.delivr.proxies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RaveProxyRequest implements Serializable{
    public static class CreateRaveSubaccountRequest{
        @JsonProperty(value = "business_name")
        public String accountName;
        @JsonProperty(value = "business_email")
        public String vendorEmail;
        @JsonProperty(value = "business_mobile")
        public String vendorPhoneNo;
        @JsonProperty(value = "business_contact")
        public String managerName;
        @JsonProperty(value = "business_contact_mobile")
        public String managerPhoneNo;
        @JsonProperty(value = "account_bank")
        public String bankCode;
        @JsonProperty(value = "account_number")
        public String accountNo;
        public String country;
        @JsonProperty(value = "split_type")
        public String splitType = "flat";
        @JsonProperty(value = "split_value")
        public Double splitValue = 0.0;
        public List<Object> meta = new ArrayList<>();
        public CreateRaveSubaccountRequest(){}
    }

    public static class AccountResolveRequest{
        @JsonProperty("account_number")
        public String accountNumber;
        @JsonProperty("account_bank")
        public String bankCode;

        public AccountResolveRequest(){}
    }

//    public static class CreateRavePaymentRequest{
//        public String amount;
//        public String currency;
//        @JsonProperty("tx_ref")
//        public String txRef;
//        @JsonProperty("redirect_url")
//        public String redirectUrl;
//        public List<SubaccountSplit> subaccounts;
//        public Customer customer;
//
//        public CreateRavePaymentRequest(){}
//    }
//    public static class SubaccountSplit{
//        @JsonProperty("transaction_charge_type")
//        public String transactionChargeType;
//        @JsonProperty("transaction_charge")
//        public Double transactionCharge;
//        @JsonProperty("id")
//        public String subaccountCode;
//        @JsonProperty("transaction_split_ratio")
//        public Double transactionSplitRatio;
//        public SubaccountSplit(){}
//    }
//    public static class Customer{
//        public String name;
//        public String phonenumber;
//        public String email;
//        public Customer(){}
//    }
//
//    public static class Customizations


}
