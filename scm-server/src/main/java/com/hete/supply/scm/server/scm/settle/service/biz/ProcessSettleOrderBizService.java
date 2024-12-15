package com.hete.supply.scm.server.scm.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleOrderExportVo;
import com.hete.supply.scm.server.scm.process.converter.ProcessOrderScanConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessSettleConverter;
import com.hete.supply.scm.server.scm.process.dao.ProcessSettleDetailReportDao;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessWithCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessSettleDetailReportPo;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.service.base.ProcessBaseService;
import com.hete.supply.scm.server.scm.settle.converter.DeductOrderConverter;
import com.hete.supply.scm.server.scm.settle.converter.ProcessSettleOrderConverter;
import com.hete.supply.scm.server.scm.settle.converter.ProcessSettleOrderItemConverter;
import com.hete.supply.scm.server.scm.settle.converter.SupplementOrderConverter;
import com.hete.supply.scm.server.scm.settle.dao.ProcessSettleOrderBillDao;
import com.hete.supply.scm.server.scm.settle.dao.ProcessSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.dao.ProcessSettleOrderItemDao;
import com.hete.supply.scm.server.scm.settle.dao.ProcessSettleOrderScanDao;
import com.hete.supply.scm.server.scm.settle.entity.dto.GetSettleOrderItemCompleteUserDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.ProcessSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.ProcessSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.ProcessSettleOrderItemDto;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderScanPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.*;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleExamine;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.scm.settle.service.base.ProcessSettleOrderBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2022/11/1 13:44
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessSettleOrderBizService {
    private final ProcessSettleOrderDao processSettleOrderDao;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessSettleOrderScanDao processSettleOrderScanDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;
    private final IdGenerateService idGenerateService;
    private final ProcessSettleOrderBaseService processSettleOrderBaseService;
    private final static int PAGE_SIZE = 500;
    private final static int MAX_ITERATIONS = 200;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProcessSettleDetailReportDao processSettleDetailReportDao;
    private final ProcessBaseService processBaseService;


    public CommonPageResult.PageInfo<ProcessSettleOrderVo> searchProcessSettleOrder(ProcessSettleOrderDto dto) {
        if (StringUtils.isNotBlank(dto.getSupplementOrderNo()) || StringUtils.isNotBlank(dto.getDeductOrderNo())) {
            String businessNo = StringUtils.isNotBlank(dto.getSupplementOrderNo()) ? dto.getSupplementOrderNo() : dto.getDeductOrderNo();
            List<ProcessSettleOrderBillPo> billPoList = processSettleOrderBillDao.getByBussinessNo(businessNo);
            if (CollectionUtil.isEmpty(billPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(billPoList.stream().map(ProcessSettleOrderBillPo::getProcessSettleOrderItemId).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            dto.setProcessSettleOrderIds(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderId).collect(Collectors.toList()));
        }
        CommonPageResult.PageInfo<ProcessSettleOrderVo> processSettleOrderVoPageInfo = processSettleOrderDao.searchProcessSettleOrder(PageDTO.of(dto.getPageNo(),
                dto.getPageSize()), dto);
        List<ProcessSettleOrderVo> records = processSettleOrderVoPageInfo.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return processSettleOrderVoPageInfo;
        }
        List<Long> idList = records.stream().map(ProcessSettleOrderVo::getProcessSettleOrderId).collect(Collectors.toList());
        List<ProcessSettleOrderItemPo> itemPoList = processSettleOrderItemDao.getByProcessSettleOrderIdList(idList);
        Map<Long, List<ProcessSettleOrderItemPo>> itemPoGroup = itemPoList.stream().collect(Collectors.groupingBy(ProcessSettleOrderItemPo::getProcessSettleOrderId));

        records.forEach(po -> {
            List<ProcessSettleOrderItemPo> processSettleOrderItemPos = itemPoGroup.get(po.getProcessSettleOrderId());
            if (CollectionUtil.isEmpty(processSettleOrderItemPos)) {
                po.setSettlePeopleNum(0);
            } else {
                po.setSettlePeopleNum(processSettleOrderItemPos.size());
            }
        });

        return processSettleOrderVoPageInfo;
    }

    public ProcessSettleOrderDetailVo getProcessSettleOrderDetail(ProcessSettleOrderDetailDto dto) {
        ProcessSettleOrderPo processSettleOrderPo = processSettleOrderDao.getById(dto.getProcessSettleOrderId());
        if (Objects.isNull(processSettleOrderPo)) {
            throw new ParamIllegalException("结算单已删除，请刷新页面或联系相关业务人员!");
        }
        ProcessSettleOrderDetailVo detailVo = ProcessSettleOrderConverter.INSTANCE.convert(processSettleOrderPo);
        List<ProcessSettleOrderItemPo> processSettleOrderItemPos = processSettleOrderItemDao.getByProcessSettleOrderId(dto.getProcessSettleOrderId());
        if (CollectionUtil.isEmpty(processSettleOrderItemPos)) {
            throw new ParamIllegalException("结算单明细已删除，请刷新页面或联系相关业务人员!");
        }

        List<ProcessSettleOrderDetailVo.ProcessSettleOrderItem> processSettleOrderItemVos = ProcessSettleOrderItemConverter.INSTANCE.processSettleOrderItemList(processSettleOrderItemPos);
        detailVo.setProcessSettleOrderItemList(processSettleOrderItemVos);

        return detailVo;
    }

    public SettleProcessOrderScanVo getSettleProcessOrderScan(ProcessSettleOrderItemDto dto) {
        ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemDao.getByProcessSettleOrderItemId(dto.getProcessSettleOrderItemId());
        if (processSettleOrderItemPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<ProcessSettleOrderScanPo> scanPos = processSettleOrderScanDao.getByProcessSettleOrderItemId(dto.getProcessSettleOrderItemId());

        SettleProcessOrderScanVo settleProcessOrderScanVo = new SettleProcessOrderScanVo();

        settleProcessOrderScanVo.setSettleProcessOrderScanDetailList(ProcessOrderScanConverter.INSTANCE.settleProcessOrderScanList(scanPos));
        BigDecimal totalProcessCommission = scanPos.stream().map(ProcessSettleOrderScanPo::getTotalProcessCommission).reduce(BigDecimal.ZERO, BigDecimal::add);
        settleProcessOrderScanVo.setTotalSettlePrice(totalProcessCommission);

        return settleProcessOrderScanVo;
    }

    public SettleSupplementOrderVo getSettleSupplementOrder(ProcessSettleOrderItemDto dto) {
        ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemDao.getByProcessSettleOrderItemId(dto.getProcessSettleOrderItemId());
        if (processSettleOrderItemPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<ProcessSettleOrderBillPo> billPos = processSettleOrderBillDao.getByProcessSettleOrderItemId(dto.getProcessSettleOrderItemId(), ProcessSettleOrderBillType.REPLENISH);

        SettleSupplementOrderVo settleSupplementOrderVo = new SettleSupplementOrderVo();

        BigDecimal totalPrice = billPos.stream().map(ProcessSettleOrderBillPo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        settleSupplementOrderVo.setTotalSettlePrice(totalPrice);
        settleSupplementOrderVo.setSupplementOrderDetailList(SupplementOrderConverter.INSTANCE.supplementOrderBillList(billPos));

        return settleSupplementOrderVo;
    }

    public SettleDeductOrderVo getSettleDeductOrder(ProcessSettleOrderItemDto dto) {
        ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemDao.getByProcessSettleOrderItemId(dto.getProcessSettleOrderItemId());
        if (processSettleOrderItemPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<ProcessSettleOrderBillPo> billPos = processSettleOrderBillDao.getByProcessSettleOrderItemId(dto.getProcessSettleOrderItemId(), ProcessSettleOrderBillType.DEDUCT);

        SettleDeductOrderVo settleDeductOrderVo = new SettleDeductOrderVo();

        BigDecimal totalPrice = billPos.stream().map(ProcessSettleOrderBillPo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        settleDeductOrderVo.setTotalSettlePrice(totalPrice);
        settleDeductOrderVo.setDeductOrderDetailList(DeductOrderConverter.INSTANCE.deductOrderBillList(billPos));

        return settleDeductOrderVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ProcessSettleOrderDetailDto dto) {
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean examine(ProcessSettleOrderExamineDto dto) {
        ProcessSettleOrderPo processSettleOrderPo = processSettleOrderDao.getByIdVersion(dto.getProcessSettleOrderId(), dto.getVersion());
        if (processSettleOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        String user = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        LocalDateTime nowTime = new DateTime().toLocalDateTime();

        processSettleOrderPo.setProcessSettleOrderId(processSettleOrderPo.getProcessSettleOrderId());
        processSettleOrderPo.setVersion(processSettleOrderPo.getVersion());

        ProcessSettleStatus processSettleStatus = processSettleOrderPo.getProcessSettleStatus();
        switch (dto.getProcessSettleExamine()) {
            case ALREADY_SETTLE:
                ProcessSettleStatus toExamine = processSettleStatus.toExamine();
                processSettleOrderPo.setProcessSettleStatus(toExamine);
                processSettleOrderPo.setExamineUser(user);
                processSettleOrderPo.setExamineUsername(username);
                processSettleOrderPo.setExamineTime(nowTime);
                break;
            case EXAMINE_AGREE:
                ProcessSettleStatus audited = processSettleStatus.toAudited();
                processSettleOrderPo.setProcessSettleStatus(audited);
                processSettleOrderPo.setSettleUser(user);
                processSettleOrderPo.setExamineRefuseRemarks(null);
                processSettleOrderPo.setSettleUsername(username);
                processSettleOrderPo.setSettleTime(nowTime);
                break;
            case EXAMINE_REFUSE:
                ProcessSettleStatus notAudited = processSettleStatus.toNotAudited();
                processSettleOrderPo.setProcessSettleStatus(notAudited);
                if (StringUtils.isBlank(dto.getExamineRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}备注", ProcessSettleExamine.EXAMINE_REFUSE.getRemark());
                }
                processSettleOrderPo.setExamineRefuseRemarks(dto.getExamineRefuseRemarks());
                processSettleOrderPo.setSettleUser(user);
                processSettleOrderPo.setSettleUsername(username);
                processSettleOrderPo.setSettleTime(nowTime);
                break;
            default:
                throw new ParamIllegalException("请求类型错误！");
        }
        processSettleOrderDao.updateByIdVersion(processSettleOrderPo);
        processSettleOrderBaseService.createStatusChangeLog(processSettleOrderPo, dto.getProcessSettleExamine());
        return true;
    }

    /**
     * 统计导出的总数
     *
     * @author ChenWenLong
     * @date 2022/12/16 17:59
     */
    public Integer getExportTotals(ProcessSettleOrderDto dto) {
        if (StringUtils.isNotBlank(dto.getSupplementOrderNo()) || StringUtils.isNotBlank(dto.getDeductOrderNo())) {
            String businessNo = StringUtils.isNotBlank(dto.getSupplementOrderNo()) ? dto.getSupplementOrderNo() : dto.getDeductOrderNo();
            List<ProcessSettleOrderBillPo> billPoList = processSettleOrderBillDao.getByBussinessNo(businessNo);
            if (CollectionUtil.isEmpty(billPoList)) {
                return 0;
            }

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(billPoList.stream().map(ProcessSettleOrderBillPo::getProcessSettleOrderItemId).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return 0;
            }
            dto.setProcessSettleOrderIds(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderId).collect(Collectors.toList()));

        }
        return processSettleOrderDao.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessSettleOrderExportVo> getExportList(ProcessSettleOrderDto dto) {
        if (StringUtils.isNotBlank(dto.getSupplementOrderNo()) || StringUtils.isNotBlank(dto.getDeductOrderNo())) {
            String businessNo = StringUtils.isNotBlank(dto.getSupplementOrderNo()) ? dto.getSupplementOrderNo() : dto.getDeductOrderNo();
            List<ProcessSettleOrderBillPo> billPoList = processSettleOrderBillDao.getByBussinessNo(businessNo);
            if (CollectionUtil.isEmpty(billPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(billPoList.stream().map(ProcessSettleOrderBillPo::getProcessSettleOrderItemId).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            dto.setProcessSettleOrderIds(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderId).collect(Collectors.toList()));
        }
        return processSettleOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }


    public CommonPageResult.PageInfo<ProcessSettleOrderDetailAndScanSettleVo> getSettleOrderScanDetailPage(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        Integer pageNo = dto.getPageNo();
        Integer pageSize = dto.getPageSize();

        IPage<ProcessSettleDetailReportPo> poPageResult
                = processSettleDetailReportDao.getByPage(new Page<>(pageNo, pageSize), dto);

        IPage<ProcessSettleOrderDetailAndScanSettleVo> voPageResult
                = ProcessSettleConverter.convertToVoPage(poPageResult);

        List<ProcessSettleOrderDetailAndScanSettleVo> records = voPageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageInfoUtil.getPageInfo(voPageResult);
        }
        List<String> processCodes = records.stream()
                .map(ProcessSettleOrderDetailAndScanSettleVo::getProcessCode)
                .collect(Collectors.toList());
        List<ProcessWithCommissionRuleBo> processWithCommissionRule
                = processBaseService.getProcessWithCommissionRule(processCodes);

        List<Long> processSettleOrderItemIds = records.stream()
                .map(ProcessSettleOrderDetailAndScanSettleVo::getProcessSettleOrderItemId)
                .collect(Collectors.toList());
        List<ProcessSettleOrderItemPo> settleOrderItemPos
                = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(processSettleOrderItemIds);

        List<ProcessSettleOrderBillPo> deductBills
                = processSettleOrderBillDao.getByProcessSettleOrderItemIds(processSettleOrderItemIds, ProcessSettleOrderBillType.DEDUCT);
        List<ProcessSettleOrderBillPo> replenishBills
                = processSettleOrderBillDao.getByProcessSettleOrderItemIds(processSettleOrderItemIds, ProcessSettleOrderBillType.REPLENISH);

        for (ProcessSettleOrderDetailAndScanSettleVo record : records) {
            String processCode = record.getProcessCode();
            Long processSettleOrderItemId = record.getProcessSettleOrderItemId();

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
                    record.setProcessType(process.getProcessType());
                }

                if (CollectionUtils.isNotEmpty(rules)) {
                    ProcessCommissionRuleBo firstCommissionLevelRule = rules.stream()
                            .filter(rule -> Objects.equals(CommissionAttribute.FIRST_LEVEL.getCommissionLevel(), rule.getCommissionLevel()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(firstCommissionLevelRule)) {
                        record.setFirstLevelStartQuantity(firstCommissionLevelRule.getStartQuantity());
                        record.setFirstLevelEndQuantity(firstCommissionLevelRule.getEndQuantity());
                        record.setFirstLevelCoefficient(firstCommissionLevelRule.getCommissionCoefficient());
                    }

                    ProcessCommissionRuleBo secondCommissionLevelRule = rules.stream()
                            .filter(rule -> Objects.equals(CommissionAttribute.SECOND_LEVEL.getCommissionLevel(), rule.getCommissionLevel()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(secondCommissionLevelRule)) {
                        record.setSecondLevelStartQuantity(secondCommissionLevelRule.getStartQuantity());
                        record.setSecondLevelEndQuantity(secondCommissionLevelRule.getEndQuantity());
                        record.setSecondLevelCoefficient(secondCommissionLevelRule.getCommissionCoefficient());
                    }
                }
            }

            // 匹配结算明细
            ProcessSettleOrderItemPo matchProcessSettleOrderItem = settleOrderItemPos.stream()
                    .filter(settleOrderItemPo -> Objects.equals(processSettleOrderItemId, settleOrderItemPo.getProcessSettleOrderItemId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcessSettleOrderItem)) {
                record.setTotalCommission(matchProcessSettleOrderItem.getSettlePrice());
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
        records.sort(
                Comparator.comparing(ProcessSettleOrderDetailAndScanSettleVo::getCompleteUser)
                        .thenComparing(ProcessSettleOrderDetailAndScanSettleVo::getProcessLabel)
        );
        return PageInfoUtil.getPageInfo(voPageResult);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSettleOrderScanDetail(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_PROCESS_ORDER_SETTLE_DETAIL.getCode(),
                        dto));
    }

    public SettleOrderItemCompleteUserVo getSettleOrderItemCompleteUsers(GetSettleOrderItemCompleteUserDto dto) {
        SettleOrderItemCompleteUserVo vo = new SettleOrderItemCompleteUserVo();
        List<ProcessSettleDetailReportPo> processSettleDetailReportPos
                = processSettleDetailReportDao.listByProcessSettleOrderId(dto.getProcessSettleOrderId());
        if (CollectionUtil.isEmpty(processSettleDetailReportPos)) {
            return vo;
        }

        // 使用Lambda表达式构建Map，处理重复键的情况
        Map<String, String> userToUsernameMap = processSettleDetailReportPos.stream()
                .collect(Collectors.toMap(
                        ProcessSettleDetailReportPo::getCompleteUser,
                        ProcessSettleDetailReportPo::getCompleteUsername,
                        (existingValue, newValue) -> existingValue
                ));

        List<SettleOrderItemCompleteUserVo.CompleteUserInfo> completeUserList = userToUsernameMap.entrySet()
                .stream()
                .map(entry -> new SettleOrderItemCompleteUserVo.CompleteUserInfo(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        vo.setCompleteUserList(completeUserList);
        return vo;
    }
}
