package com.seven.delivr.vendor;
import com.seven.delivr.requests.AppPageRequest;

public class VendorFilterRequest extends AppPageRequest {
    private String city;
    private String state;
    private String name;

    public VendorFilterRequest(){}

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
