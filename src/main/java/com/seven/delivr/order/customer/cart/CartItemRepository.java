package com.seven.delivr.order.customer.cart;

import com.seven.delivr.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItem.CartItemId> {
    List<CartItem> findAllByCartItemIdUser(User user, Pageable pageable);
    List<CartItem> findAllByCartItemIdUser(User user);
    void deleteAllByCartItemIdUser(User user);
}
