package com.seven.delivr.analytics;

import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(Constants.VERSION+"/admin")
@SecurityRequirement(name = "jwtAuth")
@RestController
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    public AnalyticsController(AnalyticsService analyticsService){
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard/orders")
    public ResponseEntity<Response> getOrderStats(){
        return Responder.ok(analyticsService.getOrderStats());
    }

    @GetMapping("/dashboard/vendor_ratings")
    public ResponseEntity<Response> getVendorRatings(AppPageRequest request){
        return Responder.ok(analyticsService.getVendorRatings(request));
    }
}
