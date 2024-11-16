package com.seven.delivr.user.location;

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

import java.util.UUID;

@RestController
@RequestMapping(value = Constants.VERSION + "/user_location")
@SecurityRequirement(name = "jwtAuth")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    @PreAuthorize(value = "hasRole('location:r')")
    public ResponseEntity<Response> getAllResources(AppPageRequest request) {
        return Responder.ok(locationService.getAll(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('location:r')")
    public ResponseEntity<Response> getResource(@Valid @NotNull @PathVariable("id") UUID id) {
        return Responder.ok(locationService.get(id));
    }

    @PostMapping
    @PreAuthorize(value =  "hasAnyAuthority('location:c', 'location:u')")
    public ResponseEntity<Response> upsertResource(@Valid @RequestBody LocationUpsertRequest request) {
        return Responder.ok(locationService.upsert(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value =  "hasAuthority('location:d')")
    public ResponseEntity<Response> deleteResource(@Valid @NotNull @PathVariable("id") UUID id) {
        locationService.delete(id);
        return Responder.noContent();
    }
    }
