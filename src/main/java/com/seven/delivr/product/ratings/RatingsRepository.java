package com.seven.delivr.product.ratings;

import com.seven.delivr.product.Product;
import com.seven.delivr.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingsRepository extends JpaRepository<Ratings, Long> {
    Integer countByProduct(Product product);
    @Query(value = "SELECT SUM(r.rating) FROM Ratings r WHERE r.product = ?1")
    Double sumRatingByProduct(Product product);
    Optional<Ratings> findByProductAndUser(Product product, User user);
}
