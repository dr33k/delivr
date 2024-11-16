package com.seven.delivr.vendor.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vendor_account")
@Data
public class VendorAccount extends BaseUUIDEntity {
    @Column(nullable = false, name = "name")
    private String alias;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    @JsonIgnore
    private Vendor vendor;
    @Column(name = "account_no", nullable = false)
    private String accountNo;
    @JsonIgnore
    @Column(name = "subaccount_code", nullable = false)
    private String subaccountCode;
    @Column(name = "bank_code", nullable = false)
    @JsonIgnore
    private String bankCode;
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = Boolean.FALSE;
    @Column
    @JsonIgnore
    private String country = "NG";

    public boolean equals(BaseUUIDEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}
