package com.seven.delivr.order.payment;

import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.customer.CustomerOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="payment")
@Data
@Builder
@AllArgsConstructor
public class Payment extends BaseUUIDEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id", referencedColumnName = "id")
    private CustomerOrder customerOrder;
    @Column(nullable = false)
    private Double amount;
    @Enumerated(EnumType.STRING)
    private PublicEnum.Currency currency;
    @Column(name = "t_ref_uuid", nullable = false)
    private UUID trefUuid;
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
    @Enumerated
    @Column(name = "payment_processor", nullable = false)
    private PublicEnum.PaymentProcessor paymentProcessor;
    @Column(name = "payment_processor_tx_id")
    private Integer paymentProcessorTxId;

    public Payment(){
        this.isPaid = Boolean.FALSE;
        this.paymentProcessor = PublicEnum.PaymentProcessor.FLUTTERWAVE;
        this.currency = PublicEnum.Currency.NGN;
    }
    public boolean equals(BaseUUIDEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}
