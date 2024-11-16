package com.seven.delivr.vendor.account;

import com.seven.delivr.base.AppService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.proxies.FlutterwaveProxy;
import com.seven.delivr.proxies.RaveProxyRequest;
import com.seven.delivr.proxies.RaveProxyResponse;
import com.seven.delivr.user.User;
import com.seven.delivr.vendor.Vendor;
import jakarta.transaction.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class VendorAccountService implements AppService{
    private final VendorAccountRepository vendorAccountRepository;
    private User principal;
    private final FlutterwaveProxy flutterwaveProxy;
    Environment environment;

    public VendorAccountService(VendorAccountRepository vendorAccountRepository,
                                User principal,
                                FlutterwaveProxy flutterwaveProxy,
                                Environment environment) {
        this.vendorAccountRepository = vendorAccountRepository;
        this.principal = principal;
        this.flutterwaveProxy = flutterwaveProxy;
        this.environment = environment;
    }

    public List<VendorAccount> getAll(AppPageRequest request) {
        return vendorAccountRepository
                .findAllByVendor(principal.getVendor(),
                        PageRequest.of(
                                request.getOffset(),
                                request.getLimit(),
                                Sort.by(Sort.Direction.DESC, "dateCreated")));
    }

    public VendorAccount get(UUID id) {
        return vendorAccountRepository.findByIdAndVendor(id, principal.getVendor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public VendorAccount create(VendorAccountCreateRequest request) {
        RaveProxyRequest.CreateRaveSubaccountRequest subaccountRequest = new RaveProxyRequest.CreateRaveSubaccountRequest();
        subaccountRequest.accountNo = request.accountNo;
        subaccountRequest.bankCode = request.bankCode;
        subaccountRequest.accountName = request.accountName;
        subaccountRequest.vendorEmail = principal.getVendor().getMgmtEmail();
        subaccountRequest.vendorPhoneNo = principal.getVendor().getPhoneNo();
        subaccountRequest.managerName = principal.getFname() + " " + principal.getLname();
        subaccountRequest.managerPhoneNo = principal.getVendor().getPhoneNo();
        subaccountRequest.country = request.country;
        RaveProxyResponse.RaveSubaccountResponse response = flutterwaveProxy.createSubaccount("Bearer "+environment.getProperty("FLUTTERWAVE_SECRET_KEY"), subaccountRequest);


        if(response.status.equals("success") || response.status.equals("successful")){
            VendorAccount vendorAccount = new VendorAccount();
            vendorAccount.setAlias(request.alias);
            vendorAccount.setCountry(request.country);
            vendorAccount.setVendor(principal.getVendor());
            vendorAccount.setBankCode(request.bankCode);
            vendorAccount.setAccountNo(request.accountNo);
            vendorAccount.setSubaccountCode(response.data.subaccountId);

            vendorAccountRepository.save(vendorAccount);
            return vendorAccount;
        }
        else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , response.message);
    }


    @Transactional
    public VendorAccount updateAlias(UUID id, String name) {
        VendorAccount vendorAccount = get(id);
        vendorAccount.setAlias(name);
        vendorAccountRepository.save(vendorAccount);
        return vendorAccount;
    }
    @Transactional
    public VendorAccount setPrimary(UUID id) {
        List<VendorAccount> vendorAccounts = vendorAccountRepository.findAllByVendor(principal.getVendor());

        vendorAccounts.forEach(pc -> pc.setIsPrimary(Boolean.FALSE));
        Optional<VendorAccount> paymentChannelOptional = vendorAccounts.stream().filter(pc -> pc.getId().equals(id)).findFirst();

        if(paymentChannelOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        VendorAccount vendorAccount = paymentChannelOptional.get();
        vendorAccount.setIsPrimary(Boolean.TRUE);

        vendorAccountRepository.saveAll(vendorAccounts);
        return vendorAccount;
    }


    @Transactional
    public void delete(UUID id) {
        VendorAccount vendorAccount = get(id);
        RaveProxyResponse.RaveSubaccountResponse response = flutterwaveProxy.deleteSubaccount("Bearer "+environment.getProperty("FLUTTERWAVE_SECRET_KEY"), vendorAccount.getSubaccountCode());
        if(response.status.equals("success"))
            vendorAccountRepository.deleteByIdAndVendor(id, principal.getVendor());
        else throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not delete at the moment, please try again later");
    }

    public RaveProxyResponse.RaveBankListResponse listBanks(){
        return flutterwaveProxy.listBanks("Bearer "+environment.getProperty("FLUTTERWAVE_SECRET_KEY"));
    }

    public RaveProxyResponse.RaveResponse resolveAccount(RaveProxyRequest.AccountResolveRequest request){
        return flutterwaveProxy.resolveAccount("Bearer "+environment.getProperty("FLUTTERWAVE_SECRET_KEY"), request);
    }
    public VendorAccount getPrimaryVendorAccount(Vendor vendor){
        List<VendorAccount> vendorAccounts = vendorAccountRepository.findAllByVendor(vendor);
        if(vendorAccounts.isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, vendor.getName() +" cannot accept payments right now. Please try another vendor");

        Stream<VendorAccount> paymentChannelStream  = vendorAccounts.stream().filter(VendorAccount::getIsPrimary);
        Optional<VendorAccount>  paymentChannelOptional = paymentChannelStream.findFirst();
        return paymentChannelOptional.orElseGet(() -> paymentChannelStream.findFirst().get());
    }
}
