package com.seven.delivr.order.customer.cart;

import com.seven.delivr.product.Product;
import com.seven.delivr.user.User;
import com.seven.delivr.util.Utilities;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "customer_cart_item")
@Data
public class CartItem{
    @EmbeddedId
    private CartItemId cartItemId;
    @Column
    private Integer units = 1;
    @Column
    private String note="";

    public CartItem(){}
    public CartItem(CartItemId cartItemId, String note, Integer units){
        this.cartItemId = cartItemId;
        this.note = Utilities.escape(note);
        this.units = units;
    }

    @Embeddable
    @Builder
    @Data
    public static class CartItemId{
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "id", name = "user_id")
        private User user;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "id", name = "product_id")
        private Product product;

        public CartItemId(){}
        public CartItemId(User user, Product product){
            this.user = user;
            this.product = product;
        }
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        public boolean equals(CartItemId cartItemId){
            return cartItemId.user.equals(this.user) && cartItemId.product.equals(this.product);
        }

        public String toString(){
            return String.format("User Id: %s\nProduct Id: %d", this.user.getId().toString(), this.product.getId());
        }
    }
}


