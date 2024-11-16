package com.seven.delivr.product.favorite;

import com.seven.delivr.base.AppService;
import com.seven.delivr.product.Product;
import com.seven.delivr.product.ProductMinifiedRecord;
import com.seven.delivr.product.ProductRepository;
import com.seven.delivr.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService implements AppService{
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final User principal;
    private final EntityManager entityManager;
    public FavoriteService(FavoriteRepository favoriteRepository,
                           ProductRepository productRepository,
                           User principal,
                           EntityManager em){
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.principal = principal;
        this.entityManager = em;
    }

    public List<ProductMinifiedRecord> getFavorites(){
        List<Favorite> favorites = favoriteRepository.findAllByFavoriteIdUserId(principal.getId());
        return productRepository.findAllById(
                favorites.stream()
                        .map(f->f.getFavoriteId().getProduct().getId())
                        .toList())
                .stream().map(ProductMinifiedRecord::map).toList();
    }

    public void add(UUID productNo){
        Product product = productRepository.findByProductNo(productNo).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        Favorite.FavoriteId fid = new Favorite.FavoriteId( entityManager.getReference(User.class, principal.getId()), product);
        if(favoriteRepository.existsById(fid)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Already added to Favorites");

        favoriteRepository.save(new Favorite(fid));
    }
    public void remove(UUID productNo){
        Product product = productRepository.findByProductNo(productNo).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        favoriteRepository.deleteById(new Favorite.FavoriteId(
                        entityManager.getReference(User.class, principal.getId()),
                        product)
        );
    }
}
