package com.hete.supply.scm.server.supplier.purchase.service.ref;

import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/5/10 11:04
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PurchaseDeliverRefService {
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;


    public PurchaseDeliverOrderPo getPurchaseDeliverOrderByNo(@NotBlank(message = "发货单号不能为空") String deliverOrderNo) {
        return purchaseDeliverOrderDao.getOneByNo(deliverOrderNo);
    }


}
