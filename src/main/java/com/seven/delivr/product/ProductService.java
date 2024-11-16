package com.seven.delivr.product;

import com.seven.delivr.base.CUService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.product.collection.ProductCollection;
import com.seven.delivr.product.collection.ProductCollectionRepository;
import com.seven.delivr.product.ratings.Ratings;
import com.seven.delivr.product.ratings.RatingsRepository;
import com.seven.delivr.user.User;
import com.seven.delivr.user.UserRole;
import com.seven.delivr.util.Utilities;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService implements CUService<ProductCreateRequest, ProductPatchRequest, UUID> {
    private final User principal;
    private final RatingsRepository ratingsRepository;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final ProductCollectionRepository productCollectionRepository;

    public ProductService(ProductRepository productRepository,
                          RatingsRepository ratingsRepository,
                          EntityManager entityManager,
                          @AuthenticationPrincipal User principal,
                          ProductCollectionRepository productCollectionRepository){
        this.productRepository = productRepository;
        this.principal = principal;
        this.ratingsRepository = ratingsRepository;
        this.entityManager = entityManager;
        this.productCollectionRepository = productCollectionRepository;
    }
    public List<ProductMinifiedRecord> getAll(AppPageRequest request) {
        return productRepository.findAllByIsPublished(Boolean.TRUE, PageRequest.of(
                request.getOffset(),
                request.getLimit(),
                Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(ProductMinifiedRecord::map).toList();
    }
    public List<ProductMinifiedRecord> getAllForVendor(AppPageRequest request) {
        return productRepository.findAllByVendor(principal.getVendor(), PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                        .stream().map(ProductMinifiedRecord::map).toList();
    }
    public Map<String, List<ProductMinifiedRecord>> getVendorItineraryForCustomer(Long vendorId, AppPageRequest request){
        List<Product> products = productRepository.findAllByVendor(entityManager.getReference(Vendor.class, vendorId), PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().toList();
        return products.stream().collect(Collectors.groupingBy(p->p.getCollection().getName(), Collectors.mapping(ProductMinifiedRecord::map, Collectors.toList())));
    }

    @Transactional(readOnly = true)
    public List<ProductMinifiedRecord> filter(ProductFilterRequest request) {
        if (request.getPriceMin() == null) request.setPriceMin(0.0);
        if (request.getPriceMax() == null) request.setPriceMax(Double.MAX_VALUE);
        if (request.getRatingMin() == null) request.setRatingMin(0.0);
        if (request.getRatingMax() == null) request.setRatingMax(5.0);
        if (request.getEtaMin() == null) request.setEtaMin(0);
        if (request.getEtaMax() == null) request.setEtaMax(Integer.MAX_VALUE);

        String queryString = "SELECT p.productNo, p.title, p.price, p.currency, p.images, p.isReady, p.ratingSum, p.noOfRatings, p.vendor.name, p.collection.name"
        +" FROM Product p ";
        int queryStringLength = queryString.length();

        if(principal.getRole() == UserRole.VEND_ADMIN || principal.getRole() == UserRole.VENDOR) {
            if(principal.getVendor() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A vendor profile has to be created first");
            queryString +=  " WHERE p.vendor = :vendor ";
        }

        if(!Utilities.isEmpty(request.getTitle())) {
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            request.setTitle(Utilities.escape(Utilities.clean(request.getTitle(), "[^A-Za-z0-9\\-\\'\\(\\) ]")).toUpperCase());
            queryString +=  " UPPER(p.title) LIKE CONCAT('%', :title, '%') ";
        }

        if(request.getType() != null){
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " p.type = :type ";
        }

        if(!Utilities.isEmpty(request.getCity())){
            request.setCity(Utilities.clean(request.getCity(),"[^a-zA-Z ]").toUpperCase());
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " p.vendor.city LIKE CONCAT('%', :city, '%') ";
        }

        if(!Utilities.isEmpty(request.getState())) {
            request.setState(Utilities.clean(request.getState(),"[^a-zA-Z ]").toUpperCase());
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " p.vendor.state LIKE CONCAT('%', :state, '%') ";
        }

        if(request.getPriceMin() > 0 || request.getPriceMax() < Double.MAX_VALUE){
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " p.price BETWEEN :priceMin AND :priceMax ";
        }

        if(request.getRatingMin() > 0 || request.getRatingMax() < 5){
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " DIV(p.ratingSum, p.noOfRatings) BETWEEN :ratingMin AND :ratingMax ";
        }
        if(request.getEtaMin() > 0 || request.getEtaMax() < Integer.MAX_VALUE){
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " SUM(p.preparationTimeMins, p.deliveryTimeMins) BETWEEN :etaMin AND :etaMax ";
        }

        queryString += " ORDER BY p.dateCreated DESC LIMIT :limit OFFSET :offset ";

        TypedQuery<Product> typedQuery = entityManager.createQuery(queryString, Product.class);

        if( principal.getRole() == UserRole.VEND_ADMIN || principal.getRole() == UserRole.VENDOR){
            typedQuery.setParameter("vendor", principal.getVendor());
        }
        if(!Utilities.isEmpty(request.getTitle())) typedQuery.setParameter("title", request.getTitle());
        if(request.getType() != null)typedQuery.setParameter("type", request.getType());
        if(!Utilities.isEmpty(request.getCity()))typedQuery.setParameter("city", request.getCity());
        if(!Utilities.isEmpty(request.getState()))typedQuery.setParameter("state", request.getState());
        if(request.getPriceMin() > 0 || request.getPriceMax() < Double.MAX_VALUE){
            typedQuery.setParameter("priceMax", request.getPriceMax());
            typedQuery.setParameter("priceMin", request.getPriceMin());
        }
        if(request.getRatingMin() > 0 || request.getRatingMax() < 5){
            typedQuery.setParameter("ratingMin", request.getRatingMin());
            typedQuery.setParameter("ratingMax", request.getRatingMax());
        }
        if(request.getEtaMin() > 0 || request.getEtaMax() < Integer.MAX_VALUE) {
            typedQuery.setParameter("etaMin", request.getEtaMin());
            typedQuery.setParameter("etaMax", request.getEtaMax());
        }
        typedQuery.setParameter("limit", request.getLimit());
        typedQuery.setParameter("offset", request.getOffset());

        return typedQuery.getResultStream().map(ProductMinifiedRecord::map).toList();
    }

    @Override
    public ProductRecord get(UUID productNo) {
        return ProductRecord.map(productRepository.findByProductNo(productNo).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional
    public ProductRecord create(ProductCreateRequest request){
        Product product = Product.creationMap(request);
        Vendor vendor = principal.getVendor();
        if(vendor == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A vendor profile has to be created first before creating a product item");
        product.setVendor(vendor);
        if(request.collectionId != null){
            ProductCollection productCollection = productCollectionRepository.findByIdAndVendor(request.collectionId, principal.getVendor())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not fond"));
            product.setCollection(productCollection);
        }
        return ProductRecord.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductRecord update(UUID productNo, ProductPatchRequest request) {
        Product product = productRepository.findByProductNo(productNo).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Product.updateMap(request, product);
        return ProductRecord.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(UUID productNo) {
        productRepository.deleteByProductNo(productNo); return;
    }

    @Transactional
    public Boolean flag(ProductFlagRequest request){
        try {
            if (!productRepository.existsByProductNo(request.getProductNo())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            if (request.getIsReady() != null) productRepository.setIsReadyByProductNo(request.getProductNo(), request.getIsReady());
            if (request.getIsPublished() != null)
                productRepository.setIsPublishedByProductNo(request.getProductNo(), request.getIsPublished());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Async
    @Transactional
    public void rate(UUID productNo, Double rating){
        try {
            Product product = productRepository.findByProductNo(productNo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            Ratings ratings = ratingsRepository.findByProductAndUser(product, principal).orElse(null);
            if(ratings == null) {
                ratings = new Ratings(product, principal);
                ratings.rating = rating;
                ratingsRepository.save(ratings);

                double ratingsSum = ratingsRepository.sumRatingByProduct(product);
                int ratingsCount = ratingsRepository.countByProduct(product);

                product.setRatingSum(ratingsSum);
                product.setNoOfRatings(ratingsCount + 1);
                productRepository.save(product);
            }
            else {
                ratings.rating = rating;
                ratingsRepository.save(ratings);

                double ratingsSum = ratingsRepository.sumRatingByProduct(product);

                product.setRatingSum(ratingsSum);
                productRepository.save(product);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}