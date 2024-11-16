package com.seven.delivr.product.ratings;

import com.seven.delivr.base.BaseLongEntity;
import com.seven.delivr.product.Product;
import com.seven.delivr.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Ratings extends BaseLongEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product product;
    @Column
    public Double rating;

    public Ratings() {}

    public Ratings(Product product, User user){
        this.user = user;
        this.product = product;
    }
}
