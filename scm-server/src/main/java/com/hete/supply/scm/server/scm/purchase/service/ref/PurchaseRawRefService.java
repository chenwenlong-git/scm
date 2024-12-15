package com.hete.supply.scm.server.scm.purchase.service.ref;

import com.hete.supply.scm.server.scm.entity.bo.RawDeliverBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.WmsDeliverBo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/10 10:27
 */
@Service
@RequiredArgsConstructor
@Validated
public class PurchaseRawRefService {
    private final PurchaseRawBaseService purchaseRawBaseService;


    /**
     * wms 原料发货
     *
     * @param wmsDeliverBo
     */
    public void wmsRawDeliver(WmsDeliverBo wmsDeliverBo,
                              List<RawDeliverBo> rawDeliverBoList) {
        purchaseRawBaseService.wmsRawDeliver(wmsDeliverBo, rawDeliverBoList);
    }


}
