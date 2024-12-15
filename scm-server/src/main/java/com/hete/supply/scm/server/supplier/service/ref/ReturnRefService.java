package com.hete.supply.scm.server.supplier.service.ref;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.converter.SupplierReturnConverter;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderBo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderItemBo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderResultBo;
import com.hete.supply.scm.server.supplier.entity.dto.ReturnOrderMqDto;
import com.hete.supply.scm.server.supplier.handler.WmsReturnOrderHandler;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/6/27 14:37
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReturnRefService {
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final IdGenerateService idGenerateService;
    private final PlmRemoteService plmRemoteService;
    private final LogBaseService logBaseService;
    private final ConsistencySendMqService consistencySendMqService;

    public PurchaseReturnOrderPo createReturnOrder(@NotNull @Valid ReturnOrderBo returnOrderBo) {
        // 创建退货单
        String returnOrderNo = idGenerateService.getConfuseCode(ScmConstant.RETURN_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        returnOrderBo.setReturnOrderNo(returnOrderNo);

        // 赋值skuEncode
        final List<ReturnOrderItemBo> purchaseReturnOrderItemBoList = returnOrderBo.getPurchaseReturnOrderItemBoList();
        final List<String> skuList = purchaseReturnOrderItemBoList.stream()
                .map(ReturnOrderItemBo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        purchaseReturnOrderItemBoList.forEach(bo -> bo.setSkuEncode(skuEncodeMap.get(bo.getSku())));

        final ReturnOrderResultBo returnOrderResultBo = SupplierReturnConverter.purchaseBoToPo(returnOrderBo);
        final PurchaseReturnOrderPo purchaseReturnOrderPo = returnOrderResultBo.getPurchaseReturnOrderPo();
        purchaseReturnOrderDao.insert(purchaseReturnOrderPo);

        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = returnOrderResultBo.getPurchaseReturnOrderItemPoList();
        if (CollectionUtils.isNotEmpty(purchaseReturnOrderItemPoList)) {
            purchaseReturnOrderItemDao.insertBatch(purchaseReturnOrderItemPoList);
        }

        // 生成退货单操作日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseReturnOrderPo.getReturnOrderNo(), ReturnOrderStatus.WAIT_MOVING.getRemark(), new ArrayList<>(),
                returnOrderBo.getOperator(), returnOrderBo.getOperatorUsername());


        // 推送生成退货单mq给wms
        final ReturnOrderMqDto returnOrderMqDto = SupplierReturnConverter.returnOrderBoToMqDto(returnOrderBo);
        consistencySendMqService.execSendMq(WmsReturnOrderHandler.class, returnOrderMqDto);

        return purchaseReturnOrderPo;
    }

}
