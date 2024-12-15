package com.hete.supply.scm.server.scm.supplier.builder;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityRuleExportVo;
import com.hete.supply.scm.server.scm.entity.bo.CalPreShelfTimeBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapDateRangeQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityLogPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityRulePo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierRestPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.supply.scm.server.scm.supplier.enums.CapacityType;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
public class SupplierCapacityBuilder {

    public static SupplierCapacityRulePo buildSupplierCapacityRulePo(String supplierCode, BigDecimal capacity, Integer period) {
        SupplierCapacityRulePo supplierCapacityPo = new SupplierCapacityRulePo();
        supplierCapacityPo.setSupplierCode(supplierCode);
        supplierCapacityPo.setCapacityType(CapacityType.NORMAL);
        supplierCapacityPo.setCapacity(capacity);
        supplierCapacityPo.setPeriod(period);
        return supplierCapacityPo;
    }

    public static List<SupplierCapacityPo> buildSupplierCapacityPos(String supplierCode,
                                                                    LocalDate beginDate, LocalDate endDate,
                                                                    BigDecimal capacity,
                                                                    Long supplierCapacityRuleId,
                                                                    List<LocalDate> restDateList) {
        List<SupplierCapacityPo> supplierCapacityPos = Lists.newArrayList();
        for (LocalDate nowDate = beginDate; !nowDate.isAfter(endDate); nowDate = nowDate.plusDays(1)) {
            SupplierCapacityPo supplierCapacityPo = new SupplierCapacityPo();
            supplierCapacityPo.setSupplierCode(supplierCode);
            supplierCapacityPo.setCapacityDate(nowDate);
            supplierCapacityPo.setTotalNormalCapacity(capacity);
            supplierCapacityPo.setNormalAvailableCapacity(capacity);

            //判断nowDate是否处于停工日期范围内
            if (restDateList.contains(nowDate)) {
                supplierCapacityPo.setTotalNormalCapacity(BigDecimal.ZERO);
                supplierCapacityPo.setNormalAvailableCapacity(BigDecimal.ZERO);
            }
            supplierCapacityPo.setSupplierCapacityRuleId(supplierCapacityRuleId);
            supplierCapacityPos.add(supplierCapacityPo);
        }
        return supplierCapacityPos;
    }

    public static SupplierRestPo buildSupplierRestPo(String supplierCode, LocalDate restDate) {
        SupplierRestPo supplierRestPo = new SupplierRestPo();
        supplierRestPo.setSupplierCode(supplierCode);
        supplierRestPo.setRestDate(restDate);
        return supplierRestPo;
    }

    public static List<SupplierCapacityPageVo.SupplierCapacityVo> buildDefaultSupplierCapacityVoList(LocalDateTime filterStartDateTime, LocalDateTime filterEndDateTime) {
        List<SupplierCapacityPageVo.SupplierCapacityVo> supplierCapacityVoList = Lists.newArrayList();
        if (Objects.isNull(filterStartDateTime) || Objects.isNull(filterEndDateTime)) {
            return supplierCapacityVoList;
        }

        LocalDate filterStartDate = TimeUtil.convertZone(filterStartDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        LocalDate filterEndDate = TimeUtil.convertZone(filterEndDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();

        //循环开始时间到结束时间的每一天
        for (LocalDate nowDate = filterStartDate; !nowDate.isAfter(filterEndDate); nowDate = nowDate.plusDays(1)) {
            SupplierCapacityPageVo.SupplierCapacityVo supplierCapacityVo = new SupplierCapacityPageVo.SupplierCapacityVo();
            supplierCapacityVo.setCapacityDate(nowDate);
            supplierCapacityVoList.add(supplierCapacityVo);
        }
        return supplierCapacityVoList;
    }

    public static List<SupCapDateRangeQueryBo> buildSupCapDateRangeQueryBoList(Set<String> supplierCodeSet, LocalDate filterStartDate, LocalDate filterEndDate) {
        List<SupCapDateRangeQueryBo> resList = Lists.newArrayList();
        supplierCodeSet.forEach(supplierCode -> {
            SupCapDateRangeQueryBo supplierCapacityQueryBo = new SupCapDateRangeQueryBo();
            supplierCapacityQueryBo.setSupplierCode(supplierCode);
            supplierCapacityQueryBo.setBeginCapacityDate(filterStartDate);
            supplierCapacityQueryBo.setEndCapacityDate(filterEndDate);
            resList.add(supplierCapacityQueryBo);
        });
        return resList;
    }

    public static List<SupplierCapacityRuleExportVo> buildSupplierCapacityRuleExportVos(List<SupplierCapacityPageVo> supplierCapacityPageVos) {
        if (CollectionUtils.isEmpty(supplierCapacityPageVos)) {
            return Lists.newArrayList();
        }

        return supplierCapacityPageVos.stream().map(supplierCapacityPageVo -> {
            SupplierCapacityRuleExportVo supplierCapacityRuleExportVo = new SupplierCapacityRuleExportVo();
            supplierCapacityRuleExportVo.setSupplierCode(supplierCapacityPageVo.getSupplierCode());
            supplierCapacityRuleExportVo.setSupplierName(supplierCapacityPageVo.getSupplierName());
            supplierCapacityRuleExportVo.setSupplierAlias(supplierCapacityPageVo.getSupplierAlias());
            supplierCapacityRuleExportVo.setSupplierStatus(Objects.nonNull(supplierCapacityPageVo.getSupplierStatus()) ?
                    supplierCapacityPageVo.getSupplierStatus().getRemark() : "");
            supplierCapacityRuleExportVo.setSupplierGrade(Objects.nonNull(supplierCapacityPageVo.getSupplierGrade()) ?
                    supplierCapacityPageVo.getSupplierGrade().getRemark() : "");
            supplierCapacityRuleExportVo.setNormalCapacity(supplierCapacityPageVo.getNormalCapacity().toString());
            supplierCapacityRuleExportVo.setRestCap30AvailCap(supplierCapacityPageVo.getFuture30TotalCap().toString());
            supplierCapacityRuleExportVo.setRestCap60AvailCap(supplierCapacityPageVo.getFuture60TotalCap().toString());
            supplierCapacityRuleExportVo.setRestCap90AvailCap(supplierCapacityPageVo.getFuture90TotalCap().toString());
            return supplierCapacityRuleExportVo;
        }).collect(Collectors.toList());
    }

    public static SupplierCapacityLogPo buildSupplierCapacityLogPo(Long supplierCapacityId,
                                                                   String bizNo,
                                                                   BigDecimal operateValue) {
        SupplierCapacityLogPo supplierCapacityLogPo = new SupplierCapacityLogPo();
        supplierCapacityLogPo.setSupplierCapacityId(supplierCapacityId);
        supplierCapacityLogPo.setBizNo(bizNo);
        supplierCapacityLogPo.setOperateCapacity(operateValue);
        return supplierCapacityLogPo;
    }

    public static List<SupplierCapacityLogPo> buildSupplierCapacityLogPos(List<SupplierCapacityPo> supplierCapacityPos,
                                                                          String bizNo,
                                                                          BigDecimal operateValue) {
        if (CollectionUtils.isEmpty(supplierCapacityPos)) {
            return Collections.emptyList();
        }
        return supplierCapacityPos.stream().map(supplierCapacityPo -> {
            SupplierCapacityLogPo supplierCapacityLogPo = new SupplierCapacityLogPo();
            supplierCapacityLogPo.setSupplierCapacityId(supplierCapacityPo.getSupplierCapacityId());
            supplierCapacityLogPo.setBizNo(bizNo);
            supplierCapacityLogPo.setOperateCapacity(operateValue);
            return supplierCapacityLogPo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierCapacityLogPo> buildSupplierCapacityLogPos(List<SupplierCapacityPo> supplierCapacityPos) {
        if (CollectionUtils.isEmpty(supplierCapacityPos)) {
            return Collections.emptyList();
        }
        return supplierCapacityPos.stream().map(supplierCapacityPo -> {
            SupplierCapacityLogPo supplierCapacityLogPo = new SupplierCapacityLogPo();
            supplierCapacityLogPo.setSupplierCapacityId(supplierCapacityPo.getSupplierCapacityId());
            supplierCapacityLogPo.setOperateCapacity(supplierCapacityPo.getTotalNormalCapacity());
            return supplierCapacityLogPo;
        }).collect(Collectors.toList());
    }

    public static CalPreShelfTimeBo buildCalPreShelfTimeBo(List<PurchasePreOrderDto.PreOrderInfoDto> preOrderInfoDtoList) {
        CalPreShelfTimeBo calPreShelfTimeBo = new CalPreShelfTimeBo();
        calPreShelfTimeBo.setPreOrderInfoDtoList(preOrderInfoDtoList);
        return calPreShelfTimeBo;
    }

    public static Deque<LocalDate> buildDequeueDate(LocalDate searchBeginDate, LocalDate searchEndDate) {
        Deque<LocalDate> searchQueue = new LinkedList<>();
        for (LocalDate date = searchBeginDate; !date.isAfter(searchEndDate); date = date.plusDays(1)) {
            searchQueue.offer(date);
        }
        return searchQueue;
    }
}
