package com.seven.delivr.product.collection;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(Constants.VERSION+"/product_collection")
@SecurityRequirement(name = "jwtAuth")
public class ProductCollectionController {
    private final ProductCollectionService productCollectionService;

    public ProductCollectionController(ProductCollectionService productCollectionService){
        this.productCollectionService = productCollectionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('collection:r')")
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
        return Responder.ok(productCollectionService.getAll(request));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('collection:r')")
    public ResponseEntity<Response> getResource(@PathVariable("id") UUID id){
        return Responder.ok(productCollectionService.get(id));
    }
    @GetMapping("/{id}/list")
    @PreAuthorize("hasAuthority('collection:r')")
    public ResponseEntity<Response> listResources(@PathVariable("id") UUID id, AppPageRequest request){
        return Responder.ok(productCollectionService.listProducts(id, request));
    }
    @PostMapping
    @PreAuthorize("hasAuthority('collection:c')")
    public ResponseEntity<Response> createResource(@Valid @RequestBody ProductCollectionUpsertRequest request){
        return Responder.ok(productCollectionService.create(request));
    }
    @PostMapping("/{id}/add")
    @PreAuthorize("hasAuthority('collection:u')")
    public ResponseEntity<Response> addProduct(@PathVariable("id") UUID id, @RequestParam("productNo") UUID productNo){
        productCollectionService.addProduct(id, productNo);
        return Responder.noContent();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('collection:u')")
    public ResponseEntity<Response> updateResource(@PathVariable("id") UUID id,  @Valid @RequestBody ProductCollectionUpsertRequest request){
        return Responder.ok(productCollectionService.update(id, request));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('collection:d')")
    public ResponseEntity<Response> deleteResource(@PathVariable("id") UUID id){
        productCollectionService.delete(id);
        return Responder.noContent();
    }
}
