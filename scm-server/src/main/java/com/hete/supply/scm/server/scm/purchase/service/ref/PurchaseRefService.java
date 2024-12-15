package com.hete.supply.scm.server.scm.purchase.service.ref;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevChildPurchaseBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopChildSubmitOrderItemDto;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author weiwenxin
 * @date 2023/6/27 10:41
 */
@Service
@RequiredArgsConstructor
public class PurchaseRefService {
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final IdGenerateService idGenerateService;
    private final LogBaseService logBaseService;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final PurchaseParentOrderChangeDao purchaseParentOrderChangeDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;

    public List<PurchaseChildOrderItemPo> getPurchaseItemByNo(String purchaseChildOrderNo) {
        return purchaseChildOrderItemDao.getListByChildNo(purchaseChildOrderNo);
    }

    public List<PurchaseChildOrderItemPo> getPurchaseItemBySkuBatchCode(List<String> skuBatchCodeList) {
        return purchaseChildOrderItemDao.getListBySkuBatchCode(skuBatchCodeList);
    }

    public void addPurchaseShippableCntByNo(@NotBlank(message = "采购子单号不能为空") String purchaseChildOrderNo,
                                            @NotNull(message = "增加的可发货数不能为空") Integer addShippableCnt) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseChildOrderNo);
        if (null == purchaseChildOrderPo) {
            throw new BizException("采购子单号：{}对应的采购子单不存在，请联系系统管理员处理！", purchaseChildOrderNo);
        }
        purchaseChildOrderPo.setShippableCnt(purchaseChildOrderPo.getShippableCnt() + addShippableCnt);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
    }

    /**
     * 开发子单创建产前样单
     *
     * @param developChildSubmitOrderItemDto:
     * @return DevChildPurchaseBo
     * @author ChenWenLong
     * @date 2024/4/8 11:07
     */
    public DevChildPurchaseBo createPurchaseOrderByDevOrder(DevelopChildSubmitOrderItemDto developChildSubmitOrderItemDto) {

        List<PurchaseParentOrderItemPo> insertPurchaseParentOrderItemPoList = new ArrayList<>();

        // 创建需求母单
        final PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
        String spu = developChildSubmitOrderItemDto.getSpu();

        int purchaseTotal = developChildSubmitOrderItemDto.getDevelopChildSubmitOrderInfoList().stream()
                .filter(Objects::nonNull)
                .mapToInt(orderInfo -> orderInfo.getPurchaseCnt() != null ? orderInfo.getPurchaseCnt() : 0)
                .sum();

        // 雪花id
        long purchaseParentOrderId = idGenerateService.getSnowflakeId();
        purchaseParentOrderPo.setPurchaseParentOrderId(purchaseParentOrderId);
        purchaseParentOrderPo.setSpu(spu);
        purchaseParentOrderPo.setPlatform(developChildSubmitOrderItemDto.getPlatform());
        purchaseParentOrderPo.setWarehouseName(developChildSubmitOrderItemDto.getWarehouseName());
        purchaseParentOrderPo.setWarehouseCode(developChildSubmitOrderItemDto.getWarehouseCode());
        purchaseParentOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(developChildSubmitOrderItemDto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.IN_PROGRESS);
        purchaseParentOrderPo.setPurchaseTotal(purchaseTotal);
        purchaseParentOrderPo.setIsDirectSend(BooleanType.FALSE);
        final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderPo.setOrderRemarks(developChildSubmitOrderItemDto.getOrderRemarks());
        purchaseParentOrderPo.setPurchaseDemandType(developChildSubmitOrderItemDto.getPurchaseDemandType());
        purchaseParentOrderPo.setSkuType(developChildSubmitOrderItemDto.getSkuType());
        purchaseParentOrderPo.setDevelopChildOrderNo(developChildSubmitOrderItemDto.getDevelopChildOrderNo());
        purchaseParentOrderPo.setSkuCnt(ScmConstant.PURCHASE_PARENT_ORDER_SKU_CNT);
        purchaseParentOrderPo.setCanSplitCnt(purchaseTotal);
        purchaseParentOrderPo.setUndeliveredCnt(purchaseTotal);
        purchaseParentOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseParentOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());

        Optional.ofNullable(developChildSubmitOrderItemDto.getDevelopChildSubmitOrderInfoList())
                .orElse(new ArrayList<>())
                .forEach(infoDto -> {
                    // 采购母单详情数据
                    final PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();
                    purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
                    purchaseParentOrderItemPo.setSku(infoDto.getSku());
                    purchaseParentOrderItemPo.setPurchaseCnt(infoDto.getPurchaseCnt());
                    purchaseParentOrderItemPo.setCanSplitCnt(infoDto.getPurchaseCnt());
                    purchaseParentOrderItemPo.setUndeliveredCnt(infoDto.getPurchaseCnt());
                    insertPurchaseParentOrderItemPoList.add(purchaseParentOrderItemPo);
                });

        purchaseParentOrderDao.insert(purchaseParentOrderPo);

        // 创建日志记录

        purchaseParentOrderPo.setUpdateTime(LocalDateTime.now());
        logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo.getPurchaseParentOrderStatus().getRemark(),
                Collections.emptyList());
        logBaseService.purchaseParentVersionLog(LogBizModule.PURCHASE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo);


        // 创建关联单据
        final PurchaseParentOrderChangePo purchaseParentOrderChangePo = new PurchaseParentOrderChangePo();
        purchaseParentOrderChangePo.setPurchaseParentOrderId(purchaseParentOrderPo.getPurchaseParentOrderId());
        purchaseParentOrderChangePo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        purchaseParentOrderChangePo.setPlaceOrderTime(new DateTime().toLocalDateTime());
        purchaseParentOrderChangePo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseParentOrderChangePo.setPlaceOrderUsername(GlobalContext.getUsername());

        purchaseParentOrderChangeDao.insert(purchaseParentOrderChangePo);

        purchaseParentOrderItemDao.insertBatch(insertPurchaseParentOrderItemPoList);

        DevChildPurchaseBo devReviewPurchaseBo = new DevChildPurchaseBo();
        devReviewPurchaseBo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        devReviewPurchaseBo.setDevelopChildOrderNo(purchaseParentOrderPo.getDevelopChildOrderNo());

        return devReviewPurchaseBo;
    }

    public PurchaseChildOrderPo getPurchaseChildPoByDeliverNo(String deliverOrderNo) {
        if (StringUtils.isBlank(deliverOrderNo)) {
            return null;
        }

        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(deliverOrderNo);
        if (null == purchaseDeliverOrderPo) {
            return null;
        }

        return purchaseChildOrderDao.getOneByChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
    }
}
