package com.seven.delivr.vendor.account;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.proxies.RaveProxyRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(Constants.VERSION+"/vendor_account")
@RestController
@PreAuthorize("hasAnyRole('VEND_ADMIN', 'VENDOR')")
@SecurityRequirement(name = "jwtAuth")
public class VendorAccountController {
    private final VendorAccountService vendorAccountService;
    public VendorAccountController(VendorAccountService vendorAccountService){
        this.vendorAccountService = vendorAccountService;
    }
    @GetMapping
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
        return Responder.ok(vendorAccountService.getAll(request));
    }
    @GetMapping("/list_banks")
    public ResponseEntity<Response> getAllBanks(){
        return Responder.ok(vendorAccountService.listBanks());
    }
    @PostMapping("/verify_account_number")
    public ResponseEntity<Response> verifyAccountNumber(@RequestBody RaveProxyRequest.AccountResolveRequest request){
        return Responder.ok(vendorAccountService.resolveAccount(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getResource(@Valid @NotNull @PathVariable UUID id){
        return Responder.ok(vendorAccountService.get(id));
    }
    @PostMapping
    public ResponseEntity<Response> createResource(@Valid @RequestBody VendorAccountCreateRequest request){
        return Responder.ok(vendorAccountService.create(request));
    }
    @PatchMapping("/{id}/modify_alias")
    public ResponseEntity<Response> updateAliasResource(@Valid @NotNull @PathVariable UUID id,@Valid @RequestParam @Pattern(regexp = "^[a-zA-Z0-9 ]*$") String newAlias){
        return Responder.ok(vendorAccountService.updateAlias(id, newAlias));
    }
    @PatchMapping("/{id}/set_primary")
    public ResponseEntity<Response> updateAliasPrimaryResource(@Valid @NotNull @PathVariable UUID id){
        return Responder.ok(vendorAccountService.setPrimary(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteResource(@Valid @NotNull @PathVariable UUID id){
        vendorAccountService.delete(id);
        return Responder.noContent();
    }
}
