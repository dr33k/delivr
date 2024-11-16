package com.seven.delivr.vendor;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.VERSION+"/vendor", produces = "application/json")
@SecurityRequirement(name = "jwtAuth")
public class VendorController {
    private final VendorService vendorService;
    public VendorController(VendorService vendorService){
        this.vendorService = vendorService;
    }

//    @GetMapping(value = "/verify", consumes = "*/*")
//    @PreAuthorize("hasRole('VENDOR')")
//    public ResponseEntity<Response> verifyVendor(@PathParam(value = "incNo")
//                                                     @Valid
//                                                     @Pattern(regexp = "[0-9]{7}")
//                                                 String incNo){
//        return Responder.ok(vendorService.verifyVendor(incNo));
//    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Response> filterResources(VendorFilterRequest request){
        return Responder.ok(this.vendorService.filter(request));
    }
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
        return Responder.ok(this.vendorService.getAll(request));
    }

    @PreAuthorize("hasAuthority('vendor:r')")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getResource(@PathVariable("id") @Valid @NotNull Long id){
        return Responder.ok(this.vendorService.get(id));
    }

    @PreAuthorize("hasAuthority('vendor:c')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> createResource(@RequestBody @Valid VendorCreateRequest request){
        var data = this.vendorService.create(request);
        return Responder.created(data, "/"+data.id());
    }

    @PreAuthorize("hasAuthority('vendor:u')")
    @PatchMapping(consumes = "application/json")
    public ResponseEntity<Response> updateResource(@RequestBody VendorPatchRequest request){
        return Responder.ok(this.vendorService.update(request));
    }

    @PreAuthorize("hasAuthority('vendor:d')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteResource(@PathVariable("id") @Valid @NotNull Long id){
        this.vendorService.delete(id);
        return Responder.noContent();
    }


}
