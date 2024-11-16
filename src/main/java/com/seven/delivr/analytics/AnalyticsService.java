package com.seven.delivr.analytics;

import com.seven.delivr.base.AppService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.order.vendor.VendorOrderRepository;
import com.seven.delivr.vendor.VendorRatingRecord;
import com.seven.delivr.vendor.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService implements AppService {
    private final VendorOrderRepository vendorOrderRepository;
    private final VendorRepository vendorRepository;
    public AnalyticsService(VendorOrderRepository vendorOrderRepository,
                            VendorRepository vendorRepository){
        this.vendorOrderRepository = vendorOrderRepository;
        this.vendorRepository = vendorRepository;
    }

    public VendorOrderStats getOrderStats(){
        VendorOrderStats stats = new VendorOrderStats();
        stats.noOfPendingTransit = vendorOrderRepository.getNoOfPendingTransit();
        stats.noOfInTransit = vendorOrderRepository.getNoOfInTransit();
        stats.noOfDelivered = vendorOrderRepository.getNoOfDelivered();
        stats.cummulativeAmount = vendorOrderRepository.getCummulativeAmount();
        stats.totalNoOfOrders = stats.noOfDelivered + stats.noOfInTransit + stats.noOfPendingTransit;
        return stats;
    }

    public List<VendorRatingRecord> getVendorRatings(AppPageRequest request){
        return vendorRepository.getVendorWithRatings(request.getLimit(), request.getOffset())
                .stream().map(VendorRatingRecord::map).toList();
    }


}
