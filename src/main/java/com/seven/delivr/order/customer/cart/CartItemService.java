package com.seven.delivr.order.customer.cart;

import com.seven.delivr.base.AppService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.product.Product;
import com.seven.delivr.product.ProductMinifiedRecord;
import com.seven.delivr.product.ProductRepository;
import com.seven.delivr.user.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartItemService implements AppService {
    private final CartItemRepository cartItemRepository;
    private User principal;
    private final EntityManager em;
    private final ProductRepository productRepository;
    public CartItemService(CartItemRepository cartItemRepository,
                           User principal,
                           EntityManager entityManager,
                           ProductRepository productRepository){
        this.cartItemRepository = cartItemRepository;
        this.principal = principal;
        this.em = entityManager;
        this.productRepository = productRepository;
    }
    public List<CartItemRecord> getAll(AppPageRequest request){
        return cartItemRepository.findAllByCartItemIdUser(em.getReference(User.class, principal.getId()), PageRequest.of(
                request.getOffset(),
                request.getLimit(),
                Sort.by(Sort.Direction.ASC, "cartItemId.product.id"))).stream().map(CartItemRecord::map).toList();

//        return cartItems.stream().collect(Collectors.groupingBy(cartItem -> cartItem.getCartItemId().getProduct().getVendor().getId(),
//                Collectors.mapping(cartItem -> new CartItemRecord(ProductMinifiedRecord.map(cartItem), cartItem.getUnits(), cartItem.getNote()), Collectors.toList())));
    }
    public Map<Long, List<CartItemRecord>> getAll(){
        List<CartItem>cartItems = cartItemRepository.findAllByCartItemIdUser(em.getReference(User.class, principal.getId()));

        return cartItems.stream().collect(Collectors.groupingBy(cartItem -> cartItem.getCartItemId().getProduct().getVendor().getId(),
                Collectors.mapping(cartItem -> new CartItemRecord(ProductMinifiedRecord.map(cartItem), cartItem.getUnits(), cartItem.getNote()), Collectors.toList())));
    }

    @Transactional
    public void add(CartItemCreateRequest request){
        Product product = productRepository.findByProductNo(request.productNo).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,  "Dish with this id does not exist"));

        CartItem.CartItemId cid = CartItem.CartItemId.builder()
                    .user(em.getReference(User.class, principal.getId()))
                    .product(product)
                    .build();
        cartItemRepository.findById(cid).ifPresentOrElse(
                cartItem -> {
                    cartItem.setUnits(request.units);
                    cartItem.setNote(request.note);
                    cartItemRepository.save(cartItem);
                },
                ()-> cartItemRepository.save(new CartItem(cid, request.note, request.units)));
    }

    @Transactional
    public void remove(UUID productNo){
        Product product = productRepository.findByProductNo(productNo).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,  "Dish with this id does not exist"));

        cartItemRepository.deleteById(CartItem.CartItemId.builder()
                .user(em.getReference(User.class, principal.getId()))
            .product(product)
                .build());
    }
    @Transactional
    public void clearCart(){
        cartItemRepository.deleteAllByCartItemIdUser(em.getReference(User.class, principal.getId()));
    }    @Transactional
    public void clearCart(UUID userId){
        cartItemRepository.deleteAllByCartItemIdUser(em.getReference(User.class, userId));
    }
}
