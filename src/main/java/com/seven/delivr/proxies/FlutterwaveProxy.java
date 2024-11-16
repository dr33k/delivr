package com.seven.delivr.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "flutterwave-api", url = "${FLUTTERWAVE_URL}")
public interface FlutterwaveProxy {
    @PostMapping(value = "/subaccounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveSubaccountResponse createSubaccount(@RequestHeader("Authorization") String token, @RequestBody RaveProxyRequest.CreateRaveSubaccountRequest request);
    @DeleteMapping(value = "/subaccounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveSubaccountResponse deleteSubaccount(@RequestHeader("Authorization") String token, @PathVariable(value = "id") String subaccountCode);

    @PostMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveResponse createPayment(@RequestHeader("Authorization") String token,@RequestBody Map<String, Object> request);
    @GetMapping(value = "/transactions/{id}/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveResponse verifyTransaction(@RequestHeader("Authorization") String token, @PathVariable(value = "id") Integer transactionId);
    @GetMapping(value = "/transactions/verify_by_reference", produces = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveResponse verifyTransaction(@RequestHeader("Authorization") String token, @RequestParam(value = "tx_ref")UUID tRefUUID);

    @GetMapping(value = "/banks/NG", produces = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveBankListResponse listBanks(@RequestHeader("Authorization") String token);
    @PostMapping(value = "/accounts/resolve", produces = MediaType.APPLICATION_JSON_VALUE)
    RaveProxyResponse.RaveResponse resolveAccount(@RequestHeader("Authorization") String token, @RequestBody RaveProxyRequest.AccountResolveRequest request);
}