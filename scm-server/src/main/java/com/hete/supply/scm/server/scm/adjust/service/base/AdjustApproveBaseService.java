package com.hete.supply.scm.server.scm.adjust.service.base;

import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.AdjustPriceApproveDao;
import com.hete.supply.scm.server.scm.adjust.dao.AdjustPriceApproveItemDao;
import com.hete.supply.scm.server.scm.adjust.entity.bo.AdjustApproveBo;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApproveItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveStatus;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/6/18 15:40
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdjustApproveBaseService {
    private final AdjustPriceApproveDao adjustPriceApproveDao;
    private final IdGenerateService idGenerateService;
    private final McRemoteService mcRemoteService;
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final AdjustPriceApproveItemDao adjustPriceApproveItemDao;

    public AdjustPriceApprovePo submitForApproval(@Valid @NotNull AdjustApproveBo bo) {
        final AdjustPriceApprovePo adjustPriceApprovePo = new AdjustPriceApprovePo();
        String adjustPriceApproveNo = idGenerateService.getConfuseCode(ScmConstant.ADJUST_PRICE_APPROVE_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        adjustPriceApprovePo.setAdjustPriceApproveNo(adjustPriceApproveNo);
        adjustPriceApprovePo.setSupplierCode(bo.getSupplierCode());
        adjustPriceApprovePo.setApproveStatus(ApproveStatus.TO_BE_APPROVE);
        adjustPriceApprovePo.setApproveType(bo.getApproveType());
        adjustPriceApprovePo.setApplyUser(GlobalContext.getUserKey());
        adjustPriceApprovePo.setApplyUsername(GlobalContext.getUsername());
        adjustPriceApprovePo.setApproveUser(bo.getApproveUser());
        adjustPriceApprovePo.setApproveUsername(bo.getApproveUsername());
        adjustPriceApprovePo.setApplyTime(LocalDateTime.now());
        adjustPriceApproveDao.insert(adjustPriceApprovePo);
        bo.setAdjustPriceApproveNo(adjustPriceApproveNo);

        // 保存明细
        final List<AdjustPriceApproveItemPo> orderApproveItemPoList = Optional.ofNullable(bo.getOrderAdjustList())
                .orElse(new ArrayList<>())
                .stream().map(orderAdjustBo -> {
                    final AdjustPriceApproveItemPo adjustPriceApproveItemPo = new AdjustPriceApproveItemPo();
                    adjustPriceApproveItemPo.setAdjustPriceApproveNo(adjustPriceApproveNo);
                    adjustPriceApproveItemPo.setSku(orderAdjustBo.getSku());
                    adjustPriceApproveItemPo.setPurchaseChildOrderNo(orderAdjustBo.getPurchaseChildOrderNo());
                    adjustPriceApproveItemPo.setPurchaseCnt(orderAdjustBo.getPurchaseCnt());
                    adjustPriceApproveItemPo.setPlatform(orderAdjustBo.getPlatform());
                    adjustPriceApproveItemPo.setOriginalPrice(orderAdjustBo.getOriginalPrice());
                    adjustPriceApproveItemPo.setAdjustPrice(orderAdjustBo.getOrderAdjust());
                    adjustPriceApproveItemPo.setAdjustReason(orderAdjustBo.getAdjustReason());
                    adjustPriceApproveItemPo.setAdjustRemark(orderAdjustBo.getRemark());

                    return adjustPriceApproveItemPo;
                }).collect(Collectors.toList());

        final List<AdjustPriceApproveItemPo> goodsApproveItemPoList = Optional.ofNullable(bo.getGoodsAdjustList())
                .orElse(new ArrayList<>())
                .stream().map(goodsAdjustBo -> {
                    final AdjustPriceApproveItemPo adjustPriceApproveItemPo = new AdjustPriceApproveItemPo();
                    adjustPriceApproveItemPo.setAdjustPriceApproveNo(adjustPriceApproveNo);
                    adjustPriceApproveItemPo.setSupplierCode(goodsAdjustBo.getSupplierCode());
                    adjustPriceApproveItemPo.setSku(goodsAdjustBo.getSku());
                    adjustPriceApproveItemPo.setOriginalPrice(goodsAdjustBo.getOriginalPrice());
                    adjustPriceApproveItemPo.setAdjustPrice(goodsAdjustBo.getAdjustPrice());
                    adjustPriceApproveItemPo.setChannelName(goodsAdjustBo.getChannel());
                    adjustPriceApproveItemPo.setEffectiveTime(goodsAdjustBo.getEffectiveTime());
                    adjustPriceApproveItemPo.setUniversal(goodsAdjustBo.getUniversal());
                    adjustPriceApproveItemPo.setEffectiveRemark(goodsAdjustBo.getEffectiveRemark());
                    adjustPriceApproveItemPo.setGoodsPriceItemId(goodsAdjustBo.getGoodsPriceItemId());

                    return adjustPriceApproveItemPo;
                }).collect(Collectors.toList());
        List<AdjustPriceApproveItemPo> newAdjustPriceApproveItemPoList = new ArrayList<>();
        newAdjustPriceApproveItemPoList.addAll(orderApproveItemPoList);
        newAdjustPriceApproveItemPoList.addAll(goodsApproveItemPoList);
        adjustPriceApproveItemDao.insertBatch(newAdjustPriceApproveItemPoList);

        AbstractApproveCreator<AdjustApproveBo> abstractApproveCreator = new AdjustPriceApproveCreator(idGenerateService,
                mcRemoteService, feishuAuditOrderDao);
        abstractApproveCreator.createFeiShuInstance(adjustPriceApprovePo.getAdjustPriceApproveId(), bo);

        return adjustPriceApprovePo;
    }
}
