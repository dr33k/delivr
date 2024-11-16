package com.seven.delivr.order.customer;

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
@RequestMapping(Constants.VERSION+"/c_order")
@SecurityRequirement(name = "jwtAuth")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;
    public CustomerOrderController(CustomerOrderService customerOrderService){
        this.customerOrderService = customerOrderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response> getAllResources(FilterPageRequest request){
        return Responder.ok(customerOrderService.getAll(request));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response> getResource(@Valid @PathVariable("id") UUID id){
        return Responder.ok(customerOrderService.get(id));
    }
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response> createResource(@Valid @RequestBody CustomerOrderCreateRequest request){
        return Responder.ok(customerOrderService.create(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response> deleteResource(@Valid @PathVariable("id") UUID id){
        customerOrderService.delete(id);
        return Responder.noContent();
    }
}
