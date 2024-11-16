package com.seven.delivr.product;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = Constants.VERSION + "/product", produces = "application/json")
@SecurityRequirement(name = "jwtAuth")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/c_list")
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
        return Responder.ok(productService.getAll(request));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/vendor_itinerary/{vendorId}")
    public ResponseEntity<Response> getVendorItinerary(@PathVariable("vendorId") @Valid Long vendorId, AppPageRequest request){
        return Responder.ok(productService.getVendorItineraryForCustomer(vendorId, request));
    }
    @PreAuthorize("hasAnyRole('VEND_ADMIN','VENDOR')")
    @GetMapping("/v_list")
    public ResponseEntity<Response> getAllResourcesForVendor(AppPageRequest request){
        return Responder.ok(productService.getAllForVendor(request));
    }

    @PreAuthorize("hasAuthority('product:r')")
    @GetMapping("/filter")
    public ResponseEntity<Response> filterResources(ProductFilterRequest request){
        return Responder.ok(productService.filter(request));
    }
    @PreAuthorize("hasAuthority('product:r')")
    @GetMapping("/{productNo}")
    public ResponseEntity<Response> getResource(@PathVariable("productNo") @Valid UUID productNo){
        return Responder.ok(productService.get(productNo));
    }

    @PreAuthorize("hasAuthority('product:c')")
    @PostMapping
    public ResponseEntity<Response> createResource(@RequestBody @Valid ProductCreateRequest request){
        var data = productService.create(request);
        return Responder.created(data, "/");
    }
    @PreAuthorize("hasAuthority('product:u')")
    @PatchMapping("/{productNo}")
    public ResponseEntity<Response> updateResource(@PathVariable("productNo") @Valid UUID productNo, @RequestBody ProductPatchRequest request){
        return Responder.ok(productService.update(productNo, request));
    }

    @PreAuthorize("hasAuthority('product:d')")
    @DeleteMapping("/{productNo}")
    public  ResponseEntity<Response> deleteResource(@PathVariable("productNo") @Valid UUID productNo){
        productService.delete(productNo);
        return Responder.noContent();
    }

    @PreAuthorize("hasAuthority('product:u')")
    @PatchMapping("/flag")
    public ResponseEntity<Response> flagResource(ProductFlagRequest request){
        if (productService.flag(request)) return Responder.noContent();
        else return Responder.conflict("Could not update product state");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("rate")
    public ResponseEntity<Response> rateResource(@RequestParam("productNo") @Valid UUID productNo, @PathParam("rating") @Valid Double rating){
        productService.rate(productNo, rating);
        return Responder.accepted("Rating submitted");
    }
}
