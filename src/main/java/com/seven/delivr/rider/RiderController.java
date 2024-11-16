package com.seven.delivr.rider;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
@RestController
@RequestMapping(Constants.VERSION+"/rider")
@SecurityRequirement(name = "jwtAuth")
public class RiderController {
    private final RiderService riderService;
    public RiderController(RiderService riderService){
        this.riderService = riderService;
    }

    @PreAuthorize("hasAuthority('rider:r')")
    @GetMapping
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
        return Responder.ok(riderService.getAll(request));
    }
    @PostMapping
    public ResponseEntity<Response> createResource(@RequestBody RiderUpsertRequest request){
        return Responder.created(riderService.upsert(request), "");
    }
    @PutMapping
    public ResponseEntity<Response> updateResource(@RequestBody RiderUpsertRequest request){
        if(request.id == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Id is missing");
        return Responder.created(riderService.upsert(request), "");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteResource(@PathVariable("id") UUID id){
        riderService.delete(id);
        return Responder.noContent();
    }


        @GetMapping(value = "/{id}/text")
    public ResponseEntity<Response> textRider(@PathVariable("id") UUID id){
        riderService.textRider(id);
        return Responder.noContent();
    }

    @GetMapping(value = "/{id}/c_order/{customerOrderId}/accept", produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateAcceptResource(@Valid @PathVariable("id") UUID id,@Valid @PathVariable("customerOrderId") UUID customerOrderId){
        return riderService.acceptOrderDelivery(id, customerOrderId);
    }

    //This here is terrible design because at the time of writing, we did not have a Rider application for the MVP
    @GetMapping(value = "/{id}/c_order/{customerOrderId}/delivered", produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateResource(@Valid @PathVariable("id") UUID id,@Valid @PathVariable("customerOrderId") UUID customerOrderId){
        return riderService.markDelivered(id, PublicEnum.OrderStatus.DELIVERED);
    }
}
