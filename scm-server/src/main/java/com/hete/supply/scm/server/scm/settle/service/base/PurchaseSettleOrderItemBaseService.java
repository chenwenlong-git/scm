package com.hete.supply.scm.server.scm.settle.service.base;

import com.hete.supply.scm.server.scm.settle.dao.PurchaseSettleOrderItemDao;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 采购结算单详情基础类
 *
 * @author ChenWenLong
 * @date 2022/1/18 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
public class PurchaseSettleOrderItemBaseService {
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;

    /**
     * 根据单据号来查询最新单条详情记录
     *
     * @author ChenWenLong
     * @date 2023/1/18 09:52
     */
    public PurchaseSettleOrderItemPo getItemByBusinessNo(String businessNo) {
        return purchaseSettleOrderItemDao.getItemByBusinessNo(businessNo);
    }
}
