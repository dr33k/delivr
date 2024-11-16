package com.seven.delivr.order.vendor;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping(Constants.VERSION+"/v_order")
@PreAuthorize("hasAnyRole('VENDOR', 'VEND_ADMIN')")
@SecurityRequirement(name = "jwtAuth")
public class VendorOrderController {
    private final VendorOrderService vendorOrderService;
    public VendorOrderController(VendorOrderService vendorOrderService){
        this.vendorOrderService = vendorOrderService;
    }
    @GetMapping
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
        return Responder.ok(vendorOrderService.getAll(request));
    }
    @GetMapping("/v_poll_filter")
    public ResponseEntity<Response> filterVendorResources(
            @RequestParam(value = "status", required = false) PublicEnum.OrderStatus status,
            @RequestParam(value = "from", required = false) ZonedDateTime from,
            AppPageRequest request){
        return Responder.ok(vendorOrderService.filterForVendor(status, from, request));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/c_poll_filter")
    public ResponseEntity<Response> filterCustomerResources(
            @RequestParam(value = "status") PublicEnum.OrderStatus status,
            @RequestParam(value = "from", required = false) ZonedDateTime from,
            AppPageRequest request){
        return Responder.ok(vendorOrderService.filterForCustomer(status, from, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getResource(@Valid @NotNull @PathVariable("id") UUID id){
        return Responder.ok(vendorOrderService.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateResource(@Valid @NotNull @PathVariable("id") UUID id, @RequestParam("status")PublicEnum.OrderStatus status){
        return Responder.ok(vendorOrderService.patch(id, status));
    }
}
