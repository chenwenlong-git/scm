package com.hete.supply.scm.server.supplier.settle.service.biz;

import cn.hutool.core.date.DateTime;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.scm.entity.bo.ReturnOrderDeductOrderBo;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.service.base.DeductOrderBaseService;
import com.hete.supply.scm.server.supplier.enums.ReturnRelatedOrderType;
import com.hete.supply.scm.server.supplier.handler.ReturnOrderConfirmStrategy;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/29 10:37
 */
@Service
@RequiredArgsConstructor
public class MaterialDefectReturnOrderConfirmStrategy implements ReturnOrderConfirmStrategy {

    private final DeductOrderBaseService deductOrderBaseService;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;

    /**
     * 原料次品的退货单产生对应扣款单
     *
     * @param purchaseReturnOrderPo:
     * @param purchaseReturnOrderItemPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/7/25 17:14
     */
    @Override
    public void createAfterConfirmReturnOrder(PurchaseReturnOrderPo purchaseReturnOrderPo,
                                              List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList) {

        //组装数据
        ReturnOrderDeductOrderBo returnOrderDeductOrderBo = new ReturnOrderDeductOrderBo();
        returnOrderDeductOrderBo.setSupplierCode(purchaseReturnOrderPo.getSupplierCode());
        returnOrderDeductOrderBo.setSupplierName(purchaseReturnOrderPo.getSupplierName());
        List<DeductOrderDefectiveDto> deductOrderDefectiveList = new ArrayList<>();
        for (PurchaseReturnOrderItemPo purchaseReturnOrderItemPo : purchaseReturnOrderItemPoList) {
            DeductOrderDefectiveDto deductOrderDefectiveDto = new DeductOrderDefectiveDto();
            deductOrderDefectiveDto.setBusinessNo(purchaseReturnOrderPo.getReturnOrderNo());
            deductOrderDefectiveDto.setSku(purchaseReturnOrderItemPo.getSku());
            deductOrderDefectiveDto.setSkuBatchCode(purchaseReturnOrderItemPo.getSkuBatchCode());
            deductOrderDefectiveDto.setDeductNum(purchaseReturnOrderItemPo.getReceiptCnt());
            deductOrderDefectiveDto.setDeductUnitPrice(purchaseReturnOrderItemPo.getDeductPrice());
            if (null == purchaseReturnOrderItemPo.getDeductPrice()) {
                throw new BizException("数据错误，退货单详情的扣款单价不能为空！");
            }
            deductOrderDefectiveDto.setDeductPrice(purchaseReturnOrderItemPo.getDeductPrice().multiply(new BigDecimal(purchaseReturnOrderItemPo.getReceiptCnt())));
            deductOrderDefectiveDto.setSettlePrice(purchaseReturnOrderItemPo.getSettlePrice());
            deductOrderDefectiveDto.setDeductRemarks("原料次品退供扣款");

            deductOrderDefectiveList.add(deductOrderDefectiveDto);
        }
        returnOrderDeductOrderBo.setDeductOrderDefectiveList(deductOrderDefectiveList);
        //调用扣款单接口创建数据
        DeductOrderPo deductOrderPo = deductOrderBaseService.addReturnOrderDeductOrder(returnOrderDeductOrderBo);
        //同步扣款单号回退货单
        purchaseReturnOrderPo.setRelatedBizNo(deductOrderPo.getDeductOrderNo());
        purchaseReturnOrderPo.setRelatedBizType(ReturnRelatedOrderType.DEDUCT_ORDER);
        purchaseReturnOrderPo.setRelatedBizTime(new DateTime().toLocalDateTime());
        purchaseReturnOrderDao.updateByIdVersion(purchaseReturnOrderPo);
    }

    @Override
    public ReturnType getHandlerType() {
        return ReturnType.MATERIAL_DEFECT;
    }
}
