package com.seven.delivr.order.customer.cart;

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
@RequestMapping(Constants.VERSION+"/cart")
@SecurityRequirement(name = "jwtAuth")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartItemController {
    private final CartItemService cartItemService;
    public CartItemController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<Response> getCartItems(AppPageRequest request){
        return Responder.ok(cartItemService.getAll(request));
    }
    @PostMapping
    public ResponseEntity<Response> addToCart(@Valid @RequestBody CartItemCreateRequest request){
        cartItemService.add(request);
        return Responder.noContent();
    }
    @DeleteMapping
    public ResponseEntity<Response> removeFromCart(@RequestParam("productNo") UUID productNo){
        cartItemService.remove(productNo);
        return Responder.noContent();
    }
}
