package com.seven.delivr.product.collection;

import com.seven.delivr.base.CUService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.product.Product;
import com.seven.delivr.product.ProductMinifiedRecord;
import com.seven.delivr.product.ProductRepository;
import com.seven.delivr.user.User;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductCollectionService implements CUService<ProductCollectionUpsertRequest, ProductCollectionUpsertRequest, UUID> {
    private final ProductCollectionRepository productCollectionRepository;
    private final User principal;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductCollectionService(ProductCollectionRepository productCollectionRepository,
                                    User principal,
                                    ProductRepository productRepository,
                                    EntityManager entityManager) {
        this.productCollectionRepository = productCollectionRepository;
        this.principal = principal;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    public List<ProductCollectionRecord> getAll(AppPageRequest request) {
        return productCollectionRepository.findAllByVendor(principal.getVendor(), PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(ProductCollectionRecord::map).toList();
    }

    @Override
    public ProductCollectionRecord get(UUID id){
        return ProductCollectionRecord.map(getCollection(id));

    }
    private ProductCollection getCollection(UUID id){
        return productCollectionRepository.findByIdAndVendor(id, principal.getVendor()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public List<ProductMinifiedRecord> listProducts(UUID collectionId, AppPageRequest request) {
        return productRepository.findAllByCollection(
                entityManager.getReference(ProductCollection.class, collectionId),
                        PageRequest.of(
                                request.getOffset(),
                                request.getLimit(),
                                Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(ProductMinifiedRecord::map).toList();
    }

    @Override
    @Transactional
    public ProductCollectionRecord create(ProductCollectionUpsertRequest request) {
        Vendor owner = principal.getVendor();
        if(productCollectionRepository.existsByNameAndVendor(request.name, owner))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A collection already exists with this name");
        ProductCollection productCollection = ProductCollection.creationMap(request);
        productCollection.setVendor(owner);
        return  ProductCollectionRecord.map(productCollectionRepository.save(productCollection));
    }
    @Transactional
    public void addProduct(UUID id, UUID productNo){
        ProductCollection productCollection = getCollection(id);
        Product product = productRepository.findByProductNo(productNo).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        product.setCollection(productCollection);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductCollectionRecord update(UUID id, ProductCollectionUpsertRequest request) {
        ProductCollection productCollection = getCollection(id);
        ProductCollection.updateMap(request, productCollection);
        return ProductCollectionRecord.map(productCollectionRepository.save(productCollection));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        productCollectionRepository.deleteByIdAndVendor(id, principal.getVendor());
    }
}
