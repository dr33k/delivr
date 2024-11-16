package com.seven.delivr.product.favorite;

import com.seven.delivr.product.Product;
import com.seven.delivr.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "favorite")
@Data
public class Favorite{
    @EmbeddedId
    private FavoriteId favoriteId;

    public Favorite(){}
    public Favorite(FavoriteId fId){
        this.favoriteId = fId;
    }

    @Embeddable
    @Builder
    @Data
    public static class FavoriteId{
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "id", name = "user_id")
        private User user;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "id", name = "product_id")
        private Product product;

        public FavoriteId(){}
        public FavoriteId(User user, Product product){
            this.user = user;
            this.product = product;
        }
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        public boolean equals(FavoriteId fId){
            return fId.user.equals(this.user) && fId.product.equals(this.product);
        }

        public String toString(){
            return String.format("User Id: %s\nProduct Id: %d", this.user.getId().toString(), this.product.getId());
        }
    }
}


