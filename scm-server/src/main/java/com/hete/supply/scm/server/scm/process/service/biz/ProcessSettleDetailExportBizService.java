package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleDetailExportVo;
import com.hete.supply.scm.server.scm.process.converter.ProcessSettleConverter;
import com.hete.supply.scm.server.scm.process.dao.ProcessSettleDetailReportDao;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessWithCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessSettleDetailReportPo;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.service.base.ProcessBaseService;
import com.hete.supply.scm.server.scm.settle.dao.ProcessSettleOrderBillDao;
import com.hete.supply.scm.server.scm.settle.dao.ProcessSettleOrderItemDao;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/18.
 */
@Service
@RequiredArgsConstructor
public class ProcessSettleDetailExportBizService {

    private final ProcessSettleDetailReportDao processSettleDetailReportDao;
    private final ProcessBaseService processBaseService;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;

    public CommonResult<ExportationListResultBo<ProcessSettleDetailExportVo>> getExportProcessSettleDetailList(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        ExportationListResultBo<ProcessSettleDetailExportVo> exportationListResultBo = new ExportationListResultBo<>();

        Integer pageNo = dto.getPageNo();
        Integer pageSize = dto.getPageSize();
        Long processSettleOrderId = dto.getProcessSettleOrderId();

        IPage<ProcessSettleDetailReportPo> poPageResult
                = processSettleDetailReportDao.getByPage(new Page<>(pageNo, pageSize), dto);
        IPage<ProcessSettleDetailExportVo> voPageResult
                = ProcessSettleConverter.convertToExportVoPage(poPageResult);

        List<ProcessSettleDetailExportVo> records = voPageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(exportationListResultBo);
        }

        Set<ProcessLabel> allProcessLabels = records.stream()
                .map(ProcessSettleDetailExportVo::getProcessLabelEnum)
                .collect(Collectors.toSet());
        Set<String> allCompleteUsers = records.stream()
                .map(ProcessSettleDetailExportVo::getCompleteUser)
                .collect(Collectors.toSet());
        List<ProcessSettleDetailReportPo> processSettleDetailReportPos
                = processSettleDetailReportDao.listByProcessSettleOrderIdAndCompleteUserAndLabel(processSettleOrderId, allProcessLabels, allCompleteUsers);

        List<Long> processSettleOrderItemIds = records.stream()
                .map(ProcessSettleDetailExportVo::getProcessSettleOrderItemId)
                .collect(Collectors.toList());
        List<ProcessSettleOrderItemPo> settleOrderItemPos
                = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(processSettleOrderItemIds);

        List<String> processCodes = records.stream()
                .map(ProcessSettleDetailExportVo::getProcessCode)
                .collect(Collectors.toList());
        List<ProcessWithCommissionRuleBo> processWithCommissionRule
                = processBaseService.getProcessWithCommissionRule(processCodes);

        List<ProcessSettleOrderBillPo> deductBills
                = processSettleOrderBillDao.getByProcessSettleOrderItemIds(processSettleOrderItemIds, ProcessSettleOrderBillType.DEDUCT);
        List<ProcessSettleOrderBillPo> replenishBills
                = processSettleOrderBillDao.getByProcessSettleOrderItemIds(processSettleOrderItemIds, ProcessSettleOrderBillType.REPLENISH);

        for (ProcessSettleDetailExportVo record : records) {
            String processCode = record.getProcessCode();
            String completeUser = record.getCompleteUser();
            ProcessLabel processLabelEnum = record.getProcessLabelEnum();
            Long processSettleOrderItemId = record.getProcessSettleOrderItemId();

            // 工序类别正品数总和
            List<ProcessSettleDetailReportPo> matchCompleteUserLabels = processSettleDetailReportPos.stream()
                    .filter(processSettleDetailReportPo ->
                            Objects.equals(completeUser, processSettleDetailReportPo.getCompleteUser())
                                    && Objects.equals(processLabelEnum, processSettleDetailReportPo.getProcessLabel()))
                    .collect(Collectors.toList());
            int processLabelQualityGoodsCnt = matchCompleteUserLabels.stream()
                    .mapToInt(ProcessSettleDetailReportPo::getQualityGoodsCnt)
                    .sum();
            record.setProcessLabelQualityGoodsCnt(processLabelQualityGoodsCnt);

            // 匹配结算明细
            ProcessSettleOrderItemPo matchProcessSettleOrderItem = settleOrderItemPos.stream()
                    .filter(settleOrderItemPo -> Objects.equals(processSettleOrderItemId, settleOrderItemPo.getProcessSettleOrderItemId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcessSettleOrderItem)) {
                record.setTotalCommission(matchProcessSettleOrderItem.getSettlePrice());
            }

            // 匹配提成规则
            ProcessWithCommissionRuleBo matchRules = processWithCommissionRule.stream()
                    .filter(rule -> Objects.equals(processCode, rule.getProcessCode()))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(matchRules)) {
                ProcessBo process = matchRules.getProcess();
                TreeSet<ProcessCommissionRuleBo> rules = matchRules.getRules();

                if (Objects.nonNull(process)) {
                    record.setProcessBasePrice(process.getCommission());
                    record.setProcessLabel(Objects.nonNull(process.getProcessLabel()) ? process.getProcessLabel()
                            .getRemark() : "");
                }

                if (CollectionUtils.isNotEmpty(rules)) {
                    ProcessCommissionRuleBo firstCommissionLevelRule = rules.stream()
                            .filter(rule -> Objects.equals(CommissionAttribute.FIRST_LEVEL.getCommissionLevel(), rule.getCommissionLevel()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(firstCommissionLevelRule)) {
                        record.setFirstLevelCoefficientAndLimit(StrUtil.format("系数：{}% 一级临界值：{}~{}", firstCommissionLevelRule.getCommissionCoefficient(),
                                firstCommissionLevelRule.getStartQuantity(), firstCommissionLevelRule.getEndQuantity()));
                    }

                    ProcessCommissionRuleBo secondCommissionLevelRule = rules.stream()
                            .filter(rule -> Objects.equals(CommissionAttribute.SECOND_LEVEL.getCommissionLevel(), rule.getCommissionLevel()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(secondCommissionLevelRule)) {
                        record.setSecondLevelCoefficientAndLimit(StrUtil.format("系数：{}% 二级临界值：{}~{}", secondCommissionLevelRule.getCommissionCoefficient(),
                                secondCommissionLevelRule.getStartQuantity(), secondCommissionLevelRule.getEndQuantity()));
                    }
                }
            }

            // 匹配 补款单 和 扣款单
            List<ProcessSettleOrderBillPo> matchDeductBills = deductBills.stream()
                    .filter(deductBill -> Objects.equals(processSettleOrderItemId, deductBill.getProcessSettleOrderItemId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(matchDeductBills)) {
                BigDecimal deductionAmount = matchDeductBills.stream()
                        .map(ProcessSettleOrderBillPo::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                record.setDeductionAmount(deductionAmount);
            }

            List<ProcessSettleOrderBillPo> matchReplenishBills = replenishBills.stream()
                    .filter(deductBill -> Objects.equals(processSettleOrderItemId, deductBill.getProcessSettleOrderItemId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(matchReplenishBills)) {
                BigDecimal additionalPayment = matchReplenishBills.stream()
                        .map(ProcessSettleOrderBillPo::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                record.setAdditionalPayment(additionalPayment);
            }
        }

        exportationListResultBo.setRowDataList(records);
        return CommonResult.success(exportationListResultBo);
    }

    public CommonResult<Integer> getProcessSettleDetailExportTotals(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        Long processSettleDetailExportTotals = processSettleDetailReportDao.getProcessSettleDetailExportTotals(dto);
        return CommonResult.success(processSettleDetailExportTotals.intValue());
    }
}
