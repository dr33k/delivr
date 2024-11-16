package com.seven.delivr.product.favorite;

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
@RequestMapping(value = Constants.VERSION + "/favorite_product", produces = "application/json")
@SecurityRequirement(name = "jwtAuth")
@PreAuthorize("hasRole('CUSTOMER')")
public class FavoriteController {
    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService){
        this.favoriteService = favoriteService;
    }
    @GetMapping
    public ResponseEntity<Response> getAllResources(){
        return Responder.ok(favoriteService.getFavorites());
    }

    @PostMapping
    public ResponseEntity<Response> createResource(@Valid @RequestParam("productNo") UUID productNo){
        favoriteService.add(productNo);
        return Responder.noContent();
    }
    @DeleteMapping("/{productNo}")
    public ResponseEntity<Response> deleteResource(@Valid @PathVariable("productNo") UUID productNo){
        favoriteService.remove(productNo);
        return Responder.noContent();
    }
}
