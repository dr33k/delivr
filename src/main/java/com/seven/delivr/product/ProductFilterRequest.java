package com.seven.delivr.product;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.enums.PublicEnum;

public class ProductFilterRequest extends AppPageRequest {
    private String title;
    private Double priceMax;
    private Double priceMin;
    private Double ratingMax;
    private Double ratingMin;
    private String city;
    private String state;
    private PublicEnum.ProductType type;
    private Integer etaMin;
    private Integer etaMax;

    public ProductFilterRequest(){
        this.priceMax = Double.MAX_VALUE;
        this.priceMin = 0.0;
        this.ratingMin = 0.0;
        this.ratingMax = 5.0;
        this.etaMin = 0;
        this.etaMax = Integer.MAX_VALUE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public Double getRatingMin() {
        return ratingMin;
    }

    public void setRatingMin(Double ratingMin) {
        this.ratingMin = ratingMin;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getRatingMax() {
        return ratingMax;
    }

    public void setRatingMax(Double ratingMax) {
        this.ratingMax = ratingMax;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PublicEnum.ProductType getType() {
        return type;
    }

    public void setType(PublicEnum.ProductType type) {
        this.type = type;
    }

    public Integer getEtaMin() {
        return etaMin;
    }

    public void setEtaMin(Integer etaMin) {
        this.etaMin = etaMin;
    }

    public Integer getEtaMax() {
        return etaMax;
    }

    public void setEtaMax(Integer etaMax) {
        this.etaMax = etaMax;
    }

}
