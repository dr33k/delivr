package com.seven.delivr.user;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Responder;
import com.seven.delivr.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = Constants.VERSION + "/user", produces = "application/json")
@SecurityRequirement(name="jwtAuth")
public class UserController {
    public final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Response> getAllResources(AppPageRequest request){
       return Responder.ok(this.userService.getAll(request));
    }

    @PreAuthorize("hasAuthority('user:r')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> getResource(@PathVariable(name = "id") UUID id){
        return Responder.ok(this.userService.get(id));
    }
    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasAuthority('user:u')")
    @Operation(description = "The id field here is required| " +
            "Set a restriction for fname and lname between 2 and 35 characters| "+
            "The postal code is nullable, but it should be 4 to 6 digits| ")
    public ResponseEntity<Response> updateResource(@RequestBody @Valid UserUpsertRequest request){
        request.operation = UserUpsertRequest.Operation.UPDATE;
        UserRecord userRecord = this.userService.upsert(request);

        return Responder.ok(userRecord);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('user:d')")
    public ResponseEntity<Response> deleteResource(@PathVariable(name = "id") UUID id){
        this.userService.delete(id);
        return Responder.noContent();
    }

}
