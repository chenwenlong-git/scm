package com.hete.supply.scm.server.scm.supplier.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuGetSuggestSupplierBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/8/5 17:17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkuGetSuggestSupplierStepTwoHandler extends AbstractSkuGetSuggestSupplierHandler {

    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;

    /**
     * 计算计划确认时间最近两个月的采购订单
     */
    private static final int PURCHASE_PLAN_CONFIRM_TIME = 2;

    @Override
    protected int sort() {
        return 2;
    }

    @Override
    protected SkuGetSuggestSupplierBo handleData(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList,
                                                 SkuGetSuggestSupplierBo resultBo) {
        log.info("推荐供应商第二步开始:入参的Dto={},入参结果Bo={}", JSON.toJSONString(dtoList), JSON.toJSONString(resultBo));
        // 入参没有对应数据，就进行下一步算法
        if (CollectionUtils.isEmpty(dtoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }
        // 实现第2步批量处理逻辑
        List<String> skuList = dtoList.stream()
                .map(SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());


        // 获取当前CN的时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);

        LocalDateTime planConfirmTimeStart = TimeUtil.convertZone(localDateTimeNow.minusMonths(PURCHASE_PLAN_CONFIRM_TIME)
                .with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS), TimeZoneId.CN, TimeZoneId.UTC);

        LocalDateTime planConfirmTimeEnd = LocalDateTime.now();

        // 查询采购订单数据
        List<PurchaseGetSuggestSupplierBo> purchaseGetSuggestSupplierBoList = purchaseChildOrderItemDao.getListBySkuListAndNotStatus(skuList,
                PurchaseOrderStatus.DELETE,
                planConfirmTimeStart,
                planConfirmTimeEnd);
        // 查询不到对应数据，就进行下一步算法
        if (CollectionUtils.isEmpty(purchaseGetSuggestSupplierBoList)) {
            log.info("推荐供应商第二步结束:查询不到对应采购数据，入参结果Bo={}", JSON.toJSONString(resultBo));
            return resultBo;
        }
        Map<String, PurchaseGetSuggestSupplierBo> purchaseGetSuggestSupplierMap = purchaseGetSuggestSupplierBoList.stream()
                .collect(Collectors.toMap(item -> item.getSupplierCode() + item.getSku(), Function.identity(), (existing, replacement) -> existing));

        // 组装返回数据
        for (SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto : dtoList) {
            // 获取更新的BO
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierListBo = resultBo.getSkuGetSuggestSupplierBoList().stream()
                    .filter(bo -> bo.getBusinessId().equals(skuAndBusinessIdBatchDto.getBusinessId()))
                    .findFirst()
                    .orElse(null);
            if (skuGetSuggestSupplierListBo == null) {
                continue;
            }
            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo> skuGetSuggestSupplierItemBos = Optional.ofNullable(skuGetSuggestSupplierListBo.getSkuGetSuggestSupplierItemBoList())
                    .orElse(new ArrayList<>());
            // 获取供应商信息与采购获取供应商进行取交集
            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo> intersectedSupplierList = skuGetSuggestSupplierItemBos.stream()
                    .filter(item -> purchaseGetSuggestSupplierMap.containsKey(item.getSupplierCode() + skuAndBusinessIdBatchDto.getSku()))
                    .peek(item -> {
                        PurchaseGetSuggestSupplierBo purchaseGetSuggestSupplierBo = purchaseGetSuggestSupplierMap.get(item.getSupplierCode() + skuAndBusinessIdBatchDto.getSku());
                        item.setPurchaseChildOrderNo(purchaseGetSuggestSupplierBo.getPurchaseChildOrderNo());
                        item.setPlanConfirmTime(purchaseGetSuggestSupplierBo.getPlanConfirmTime());
                    }).collect(Collectors.toList());
            log.info("推荐供应商第二步结束:业务ID:{}的匹配采购信息获取对应结果Bo=>{}", skuAndBusinessIdBatchDto.getBusinessId(), JSON.toJSONString(intersectedSupplierList));
            // 仅有一个则标记默认而且返回结果
            if (CollectionUtils.isNotEmpty(intersectedSupplierList) && intersectedSupplierList.size() == 1) {
                intersectedSupplierList.get(0).setIsDefault(BooleanType.TRUE);
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(intersectedSupplierList);
                continue;
            }
            // 如果交集多个更新为交集的供应商
            if (CollectionUtils.isNotEmpty(intersectedSupplierList) && intersectedSupplierList.size() > 1) {
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(intersectedSupplierList);
                continue;
            }
        }

        log.info("推荐供应商第二步结束:入参结果Bo={}", JSON.toJSONString(resultBo));
        return resultBo;
    }
}