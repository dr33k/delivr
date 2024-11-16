package com.seven.delivr.order.payment;

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
@RequestMapping(Constants.VERSION+"/payment")
@SecurityRequirement(name = "jwtAuth")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }
    @PostMapping(value = "/pay")
    @PreAuthorize("hasAuthority('payment:c')")
    public ResponseEntity<Response> initiatePayment(@RequestBody @Valid PaymentRequest paymentRequest){
        //Step 1: Initiate Payment
        String redirectUrl = paymentService.initiatePayment(paymentRequest);
        //Step 2: Redirect to make payment with Payment Processor
        return Responder.ok(redirectUrl);
    }
    @GetMapping(value = "/callback")
    public ResponseEntity<Response> callBack(@RequestParam("tx_ref") UUID tRefUUID, @RequestParam("transaction_id") Integer txId){
    //Step 3: Validate payment
        return Responder.ok(paymentService.validatePayment(tRefUUID, txId));
    }
    @GetMapping(value = "/requery")
    @PreAuthorize("hasAuthority('payment:r')")
    public ResponseEntity<Response> requeryPayment(@RequestParam("tx_ref") UUID tRefUUID){
        return Responder.ok(paymentService.validatePayment(tRefUUID, null));
    }
}
