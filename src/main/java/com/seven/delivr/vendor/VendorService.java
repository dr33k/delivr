package com.seven.delivr.vendor;

import com.seven.delivr.base.CUService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.product.Product;
import com.seven.delivr.product.ProductRepository;
import com.seven.delivr.user.User;
import com.seven.delivr.user.UserRepository;
import com.seven.delivr.util.Utilities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VendorService implements CUService<VendorCreateRequest, VendorPatchRequest, Long> {
//    private final VendorVerificationProxy vendorVerificationProxy;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private User principal;


    public VendorService(
            VendorRepository vendorRepository,
            UserRepository userRepository,
            EntityManager entityManager,
            @AuthenticationPrincipal User principal,
            ProductRepository productRepository){
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.principal = principal;
        this.entityManager = entityManager;
        this.productRepository = productRepository;
    }
    public List<VendorMinifiedRecord> filter(VendorFilterRequest request) {
        String queryString = "SELECT v.id, v.name, v.mgmtEmail, v.logo, v.dateCreated"
                +" FROM Vendor v";
        int queryStringLength = queryString.length();

        if(!Utilities.isEmpty(request.getCity())){
            request.setCity(Utilities.clean(request.getCity(),"[^a-zA-Z ]").toUpperCase());
            queryString += " WHERE v.city LIKE CONCAT('%', :city, '%') ";
        }

        if(!Utilities.isEmpty(request.getState())) {
            request.setState(Utilities.clean(request.getState(),"[^a-zA-Z ]").toUpperCase());
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " v.state LIKE CONCAT('%', :state, '%') ";
        }
        if(!Utilities.isEmpty(request.getName())) {
            String name= request.getName();
            request.setName(Utilities.clean(name,"[^A-Za-z0-9 ]")
                    .substring(Math.min(name.length() - 1, 1)));
            queryString += (queryString.length() > queryStringLength)? " AND " : " WHERE ";
            queryString += " v.name LIKE CONCAT('%', :name, '%') ";
        }

        queryString += " ORDER BY v.dateCreated DESC LIMIT :limit OFFSET :offset";

        TypedQuery<Vendor> typedQuery = entityManager.createQuery(queryString, Vendor.class);
        if(!Utilities.isEmpty(request.getCity())) typedQuery.setParameter("city", request.getCity());
        if(!Utilities.isEmpty(request.getState())) typedQuery.setParameter("state", request.getState());
        if(!Utilities.isEmpty(request.getName())) typedQuery.setParameter("name", request.getName());
        typedQuery.setParameter("limit", request.getLimit());
        typedQuery.setParameter("offset", request.getOffset());

        return typedQuery.getResultStream().map(VendorMinifiedRecord::map).toList();
    }
    public List<VendorMinifiedRecord> getAll(AppPageRequest request) {
        Set<Vendor> vendorSet = vendorRepository.findAll(PageRequest.of(
                        request.getOffset(),
                        request.getLimit(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().collect(Collectors.toSet());

        return vendorSet.stream().map(VendorMinifiedRecord::map).toList();
    }

    @Override
    public VendorRecord get(Long id) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return VendorRecord.map(vendor);
    }
    @Override
    @Transactional
    public VendorRecord create(VendorCreateRequest request) {
        if(principal.getVendor() != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user is already associated with a Vendor");
        if (vendorRepository.existsByMgmtEmail(request.mgmtEmail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already taken");
//        if (vendorRepository.existsByIncorporationNo(Integer.parseInt(request.incorporationNo)))
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "A business with this CAC number already exists");

        Vendor vendor = Vendor.creationMap(request);
        vendorRepository.save(vendor);

        userRepository.setVendorById(principal.getId(), vendor.getId());
        return VendorRecord.map(vendor);
    }
    @Transactional
    public VendorRecord update(VendorPatchRequest request) {
        if (request.id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not specified");
        if (!request.id.equals(principal.getVendor().getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access to Vendor Account");

        Vendor vendor = this.vendorRepository.findById(request.id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Vendor.updateMap(request, vendor);

        vendorRepository.save(vendor);
        return VendorRecord.map(vendor);
    }

    @Override
    public <RES> RES update(Long aLong, VendorPatchRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!id.equals(principal.getVendor().getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access to Vendor Account");
        vendorRepository.deleteById(id);
    }

//    public CACResponse.CACResponseData verifyVendor(String incorporationNo) {
//        try {
//            VendorVerificationProxyRequest request = new VendorVerificationProxyRequest(incorporationNo);
//            CACResponse response = vendorVerificationProxy.queryCAC(request);
//
//
//            if ("OK".equals(response.status)) {
//                if (response.data == null)
//                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CAC lookup: Business with RC number provided not found");
//
//                return response.data.get(0);
//            } else
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "Sorry, we couldn't find this business");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sorry, It seems we are unable to verify this business right now. Try again later");
//        }
//    }

    @Scheduled(cron = "00 00 00 ? * Sun")
    public void updateVendorRatings(){
        log.info(ZonedDateTime.now().toString()+ "Scheduled Vendor rating task");
        List<Vendor> vendors = vendorRepository.getVendorWithRatings();
        vendors.forEach(vendor -> {
            List<Product> products = productRepository.findAllWithRatingByVendor(vendor);
                double sumOfRatings = products.stream()
                        .mapToDouble(product-> product.getRatingSum() / product.getNoOfRatings())
                        .reduce(Double::sum).orElse(0);

                    MathContext mc = new MathContext(2);
                vendor.setRating(new BigDecimal(sumOfRatings/products.size()).round(mc).doubleValue());
                });

        vendorRepository.saveAllAndFlush(vendors);
    }
}
