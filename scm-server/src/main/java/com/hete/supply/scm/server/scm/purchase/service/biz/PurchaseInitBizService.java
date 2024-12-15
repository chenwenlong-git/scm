package com.hete.supply.scm.server.scm.purchase.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/17 15:17
 */
@Service
@RequiredArgsConstructor
public class PurchaseInitBizService {
    private final static Integer MAX_LOOP = 1000;
    private final static Integer PAGE_SIZE = 200;

    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;

    /**
     * 统一原料使用方式
     */
    @Transactional(rollbackFor = Exception.class)
    public void purchaseRawChangeTask() {
        for (int i = 1; i < MAX_LOOP; i++) {
            final CommonPageResult.PageInfo<PurchaseChildOrderRawPo> pageInfo = purchaseChildOrderRawDao.getListByPage(PageDTO.of(1, PAGE_SIZE),
                    Arrays.asList(PurchaseRawBizType.RECORD, PurchaseRawBizType.RECORD_BOM));

            final List<PurchaseChildOrderRawPo> records = pageInfo.getRecords();

            if (records.size() == 0) {
                return;
            }
            final List<PurchaseChildOrderRawPo> newPoList = new ArrayList<>();
            for (PurchaseChildOrderRawPo record : records) {
                if (PurchaseRawBizType.RECORD.equals(record.getPurchaseRawBizType())) {
                    record.setPurchaseRawBizType(PurchaseRawBizType.DEMAND);
                    if (record.getActualConsumeCnt() > 0 || record.getExtraCnt() > 0) {
                        final PurchaseChildOrderRawPo purchaseChildOrderRawPo = new PurchaseChildOrderRawPo();
                        purchaseChildOrderRawPo.setPurchaseParentOrderNo(record.getPurchaseParentOrderNo());
                        purchaseChildOrderRawPo.setPurchaseChildOrderNo(record.getPurchaseChildOrderNo());
                        purchaseChildOrderRawPo.setSku(record.getSku());
                        purchaseChildOrderRawPo.setSkuBatchCode(record.getSkuBatchCode());
                        purchaseChildOrderRawPo.setDeliveryCnt(record.getActualConsumeCnt() + record.getExtraCnt());
                        purchaseChildOrderRawPo.setPurchaseRawBizType(PurchaseRawBizType.ACTUAL_DELIVER);
                        purchaseChildOrderRawPo.setRawSupplier(record.getRawSupplier());
                        purchaseChildOrderRawPo.setRawExtra(RawExtra.NORMAL);
                        purchaseChildOrderRawPo.setActualConsumeCnt(record.getActualConsumeCnt());
                        purchaseChildOrderRawPo.setExtraCnt(record.getExtraCnt());
                        purchaseChildOrderRawPo.setReceiptCnt(record.getActualConsumeCnt() + record.getExtraCnt());
                        purchaseChildOrderRawPo.setDispenseCnt(record.getDispenseCnt());
                        newPoList.add(purchaseChildOrderRawPo);
                    }
                } else if (PurchaseRawBizType.RECORD_BOM.equals(record.getPurchaseRawBizType())) {
                    record.setPurchaseRawBizType(PurchaseRawBizType.FORMULA);
                }
            }
            purchaseChildOrderRawDao.updateBatchByIdVersion(records);

            if (CollectionUtils.isNotEmpty(newPoList)) {
                purchaseChildOrderRawDao.insertBatch(newPoList);
            }
        }
    }
}
