package com.seven.delivr.auth;

import com.seven.delivr.auth.verification.email.OTPConfirmationRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.user.User;
import com.seven.delivr.user.UserRecord;
import com.seven.delivr.user.UserService;
import com.seven.delivr.user.UserUpsertRequest;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constants.VERSION + "/auth", produces = "application/json")
public class AuthController {
    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;

    public AuthController(UserService userService, AuthenticationProvider authenticationProvider) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping(value = "/send_otp")
    public ResponseEntity<Response> sendOtp(
            @RequestParam("username")
            @Valid @Email(regexp = "^.+@.+\\..+$", message = "Incorrect email format")
            String username) {
        userService.sendOtp(username);
        return Responder.accepted("Verification email sent");
    }

    @PostMapping(value = "/confirm_otp", consumes = "application/json")
    public ResponseEntity<Response> confirmEmail(@RequestBody @Valid OTPConfirmationRequest request) {
        return this.userService.otpIsValid(request.pin, request.username) ?
        Responder.ok("OTP validated successfully")
        : Responder.conflict("Invalid OTP");
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest request) {
         User user = (User) authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username, request.password)).getPrincipal();

         String token = userService.login(user);
         return Responder.ok(UserRecord.map(user), token);

    }

    @PostMapping(value = "/register", consumes = "application/json")
    @Operation(description = "The id field here is ignored. The actual id will be returned on creation| "+
            "Set a restriction for fname and lname between 2 and 35 characters| "+
            "The postal code is nullable, but it should be 4 to 6 digits| ")
    public ResponseEntity<Response> createResource(@RequestBody @Valid UserUpsertRequest request){
        request.operation = UserUpsertRequest.Operation.CREATE;
        UserRecord userRecord = this.userService.upsert(request);

        String token = userService.login(userRecord);

        return Responder.created( userRecord, "/", token);
    }
}
