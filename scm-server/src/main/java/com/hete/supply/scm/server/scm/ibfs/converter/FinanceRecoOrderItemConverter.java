package com.hete.supply.scm.server.scm.ibfs.converter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.RecoOrderItemCreateDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemInspectVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemRelationVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemSkuDetailVo;
import com.hete.support.api.enums.BooleanType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/5/21 19:29
 */
public class FinanceRecoOrderItemConverter {


    public static FinanceRecoOrderItemPo dtoToPo(FinanceRecoOrderPo financeRecoOrderPo,
                                                 RecoOrderItemCreateDto dto,
                                                 String collectOrderNo) {
        FinanceRecoOrderItemPo financeRecoOrderItemPo = new FinanceRecoOrderItemPo();
        financeRecoOrderItemPo.setFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        financeRecoOrderItemPo.setCollectOrderNo(collectOrderNo);
        financeRecoOrderItemPo.setSupplierCode(financeRecoOrderPo.getSupplierCode());
        financeRecoOrderItemPo.setFinanceRecoFundType(dto.getFinanceRecoFundType());
        financeRecoOrderItemPo.setCollectOrderType(dto.getCollectOrderType());
        financeRecoOrderItemPo.setFinanceRecoPayType(dto.getCollectOrderType().getFinanceRecoPayType());
        financeRecoOrderItemPo.setNum(dto.getNum());
        financeRecoOrderItemPo.setTotalPrice(dto.getTotalPrice());
        return financeRecoOrderItemPo;
    }

    public static FinanceRecoOrderItemSkuPo itemPoToItemSkuPo(FinanceRecoOrderItemPo financeRecoOrderItemPo,
                                                              RecoOrderItemCreateDto dto,
                                                              String collectOrderNo) {
        FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = new FinanceRecoOrderItemSkuPo();
        financeRecoOrderItemSkuPo.setFinanceRecoOrderItemId(financeRecoOrderItemPo.getFinanceRecoOrderItemId());
        financeRecoOrderItemPo.setCollectOrderNo(collectOrderNo);
        financeRecoOrderItemSkuPo.setFinanceRecoOrderNo(financeRecoOrderItemPo.getFinanceRecoOrderNo());
        financeRecoOrderItemSkuPo.setCollectOrderNo(financeRecoOrderItemPo.getCollectOrderNo());
        financeRecoOrderItemSkuPo.setFinanceRecoFundType(financeRecoOrderItemPo.getFinanceRecoFundType());
        financeRecoOrderItemSkuPo.setCollectOrderType(financeRecoOrderItemPo.getCollectOrderType());
        financeRecoOrderItemSkuPo.setFinanceRecoPayType(financeRecoOrderItemPo.getFinanceRecoPayType());
        financeRecoOrderItemSkuPo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.WAIT_CONFIRM);
        financeRecoOrderItemSkuPo.setSupplierCode(financeRecoOrderItemPo.getSupplierCode());
        financeRecoOrderItemSkuPo.setNum(dto.getNum());
        financeRecoOrderItemSkuPo.setPrice(dto.getPrice());
        financeRecoOrderItemSkuPo.setTotalPrice(dto.getTotalPrice());
        financeRecoOrderItemSkuPo.setRemarks(dto.getRemarks());
        financeRecoOrderItemSkuPo.setAssociationTime(LocalDateTime.now());

        return financeRecoOrderItemSkuPo;
    }


    public static FinanceRecoOrderItemPo collectToItemPo(@NotNull FinanceRecoOrderPo financeRecoOrderPo,
                                                         @NotNull FinanceRecoFundType financeRecoFundType,
                                                         @NotNull CollectOrderType collectOrderType,
                                                         @NotBlank String collectOrderNo,
                                                         @NotNull Long financeRecoOrderItemId) {
        FinanceRecoOrderItemPo financeRecoOrderItemPo = new FinanceRecoOrderItemPo();
        financeRecoOrderItemPo.setFinanceRecoOrderItemId(financeRecoOrderItemId);
        financeRecoOrderItemPo.setFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        financeRecoOrderItemPo.setSupplierCode(financeRecoOrderPo.getSupplierCode());
        financeRecoOrderItemPo.setFinanceRecoFundType(financeRecoFundType);
        financeRecoOrderItemPo.setCollectOrderType(collectOrderType);
        financeRecoOrderItemPo.setFinanceRecoPayType(collectOrderType.getFinanceRecoPayType());
        financeRecoOrderItemPo.setCollectOrderNo(collectOrderNo);
        return financeRecoOrderItemPo;
    }

    public static FinanceRecoOrderItemSkuPo collectToItemSkuPo(@NotNull FinanceRecoOrderItemPo financeRecoOrderItemPo,
                                                               @NotNull Integer num,
                                                               @NotNull BigDecimal price,
                                                               @NotNull BigDecimal totalPrice,
                                                               @NotNull Long collectOrderItemId,
                                                               @NotNull Long financeRecoOrderItemSkuId,
                                                               String sku,
                                                               String skuBatchCode) {
        FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = new FinanceRecoOrderItemSkuPo();
        financeRecoOrderItemSkuPo.setFinanceRecoOrderItemSkuId(financeRecoOrderItemSkuId);
        financeRecoOrderItemSkuPo.setFinanceRecoOrderItemId(financeRecoOrderItemPo.getFinanceRecoOrderItemId());
        financeRecoOrderItemSkuPo.setFinanceRecoOrderNo(financeRecoOrderItemPo.getFinanceRecoOrderNo());
        financeRecoOrderItemSkuPo.setCollectOrderNo(financeRecoOrderItemPo.getCollectOrderNo());
        financeRecoOrderItemSkuPo.setFinanceRecoFundType(financeRecoOrderItemPo.getFinanceRecoFundType());
        financeRecoOrderItemSkuPo.setCollectOrderType(financeRecoOrderItemPo.getCollectOrderType());
        financeRecoOrderItemSkuPo.setFinanceRecoPayType(financeRecoOrderItemPo.getFinanceRecoPayType());
        financeRecoOrderItemSkuPo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.WAIT_CONFIRM);
        financeRecoOrderItemSkuPo.setSupplierCode(financeRecoOrderItemPo.getSupplierCode());
        financeRecoOrderItemSkuPo.setCollectOrderItemId(collectOrderItemId);
        financeRecoOrderItemSkuPo.setNum(num);
        if (StringUtils.isNotBlank(sku)) {
            financeRecoOrderItemSkuPo.setSku(sku);
        }
        if (StringUtils.isNotBlank(skuBatchCode)) {
            financeRecoOrderItemSkuPo.setSkuBatchCode(skuBatchCode);
        }
        financeRecoOrderItemSkuPo.setPrice(price);
        financeRecoOrderItemSkuPo.setTotalPrice(totalPrice);
        financeRecoOrderItemSkuPo.setAssociationTime(LocalDateTime.now());

        return financeRecoOrderItemSkuPo;
    }

    public static RecoOrderItemSkuDetailVo poToVo(FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo,
                                                  List<FinanceRecoOrderItemInspectPo> financeRecoOrderItemInspectPoList,
                                                  List<FinanceRecoOrderItemRelationPo> financeRecoOrderItemRelationPoList,
                                                  List<String> scmFileList) {

        RecoOrderItemSkuDetailVo recoOrderItemSkuDetailVo = new RecoOrderItemSkuDetailVo();
        recoOrderItemSkuDetailVo.setFinanceRecoOrderItemSkuId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());
        recoOrderItemSkuDetailVo.setVersion(financeRecoOrderItemSkuPo.getVersion());
        recoOrderItemSkuDetailVo.setFinanceRecoOrderItemId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemId());
        recoOrderItemSkuDetailVo.setFinanceRecoOrderNo(financeRecoOrderItemSkuPo.getFinanceRecoOrderNo());
        recoOrderItemSkuDetailVo.setCollectOrderNo(financeRecoOrderItemSkuPo.getCollectOrderNo());
        recoOrderItemSkuDetailVo.setFinanceRecoFundType(financeRecoOrderItemSkuPo.getFinanceRecoFundType());
        recoOrderItemSkuDetailVo.setCollectOrderType(financeRecoOrderItemSkuPo.getCollectOrderType());
        recoOrderItemSkuDetailVo.setAssociationTime(financeRecoOrderItemSkuPo.getAssociationTime());
        recoOrderItemSkuDetailVo.setSku(financeRecoOrderItemSkuPo.getSku());
        recoOrderItemSkuDetailVo.setSkuBatchCode(financeRecoOrderItemSkuPo.getSkuBatchCode());
        recoOrderItemSkuDetailVo.setPrice(financeRecoOrderItemSkuPo.getPrice());
        recoOrderItemSkuDetailVo.setNum(financeRecoOrderItemSkuPo.getNum());
        recoOrderItemSkuDetailVo.setTotalPrice(financeRecoOrderItemSkuPo.getTotalPrice());
        recoOrderItemSkuDetailVo.setRemarks(financeRecoOrderItemSkuPo.getRemarks());
        recoOrderItemSkuDetailVo.setFinanceRecoPayType(financeRecoOrderItemSkuPo.getFinanceRecoPayType());
        recoOrderItemSkuDetailVo.setRecoOrderItemSkuStatus(financeRecoOrderItemSkuPo.getRecoOrderItemSkuStatus());
        List<FinanceRecoOrderItemInspectPo> inspectPoList = Optional.ofNullable(financeRecoOrderItemInspectPoList)
                .orElse(new ArrayList<>());
        List<RecoOrderItemInspectVo> recoOrderItemInspectList = inspectPoList
                .stream()
                .map(po -> {
                    RecoOrderItemInspectVo recoOrderItemInspectVo = new RecoOrderItemInspectVo();
                    recoOrderItemInspectVo.setRecoOrderInspectType(po.getRecoOrderInspectType());
                    recoOrderItemInspectVo.setOriginalValue(po.getOriginalValue());
                    recoOrderItemInspectVo.setInspectValue(po.getInspectValue());
                    recoOrderItemInspectVo.setInspectResult(po.getInspectResult());
                    if (null != financeRecoOrderItemSkuPo.getCollectOrderType()) {
                        recoOrderItemInspectVo.setInspectResultRule(financeRecoOrderItemSkuPo.getCollectOrderType().getInspectRule(po.getRecoOrderInspectType()));
                    }
                    return recoOrderItemInspectVo;
                }).collect(Collectors.toList());

        Integer inspectTotalNum = (int) inspectPoList.stream().filter(financeRecoOrderItemInspectPo -> BooleanType.FALSE.equals(
                        financeRecoOrderItemInspectPo.getInspectResult()))
                .count();

        List<FinanceRecoOrderItemRelationPo> relationPoList = Optional.ofNullable(financeRecoOrderItemRelationPoList)
                .orElse(new ArrayList<>());
        List<RecoOrderItemRelationVo> recoOrderItemRelationList = relationPoList.stream()
                .map(po -> {
                    RecoOrderItemRelationVo recoOrderItemRelationVo = new RecoOrderItemRelationVo();
                    recoOrderItemRelationVo.setRecoOrderItemRelationType(po.getRecoOrderItemRelationType());
                    recoOrderItemRelationVo.setBusinessNo(po.getBusinessNo());
                    recoOrderItemRelationVo.setSku(po.getSku());
                    recoOrderItemRelationVo.setNum(po.getNum());
                    recoOrderItemRelationVo.setTotalPrice(po.getTotalPrice());
                    return recoOrderItemRelationVo;
                }).collect(Collectors.toList());

        recoOrderItemSkuDetailVo.setInspectTotalNum(inspectTotalNum);
        recoOrderItemSkuDetailVo.setRecoOrderItemInspectList(recoOrderItemInspectList);
        recoOrderItemSkuDetailVo.setRecoOrderItemRelationList(recoOrderItemRelationList);

        recoOrderItemSkuDetailVo.setFileCodeList(scmFileList);

        return recoOrderItemSkuDetailVo;
    }

}
