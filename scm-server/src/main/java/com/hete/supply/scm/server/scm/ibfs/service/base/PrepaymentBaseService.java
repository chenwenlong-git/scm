package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowTransferDto;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.ibfs.dao.FinancePrepaymentOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoPrepaymentDao;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.PrepaymentBo;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.PrepaymentNoListDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.PrepaymentSupplierDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.PrepaymentTransferDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.PrepaymentTransferItemDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePrepaymentOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoPrepaymentPo;
import com.hete.supply.scm.server.scm.ibfs.enums.DeductionStatus;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/5/21 10:57
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrepaymentBaseService {
    private final FinancePrepaymentOrderDao financePrepaymentOrderDao;
    private final FinanceRecoPrepaymentDao financeRecoPrepaymentDao;
    private final McRemoteService mcRemoteService;
    private final LogBaseService logBaseService;

    /**
     * 获取供应商所有的可抵扣金额
     *
     * @param supplierCode
     * @return
     */
    public BigDecimal getAllCanDeductionMoney(String supplierCode) {
        return financePrepaymentOrderDao.getAllCanDeductionMoney(supplierCode, PrepaymentOrderStatus.DELETED);
    }

    /**
     * 近3个月预付款金额
     *
     * @param supplierCode
     * @return
     */
    public BigDecimal getRecentlyPrepaymentMoney(String supplierCode) {
        LocalDateTime now = LocalDateTime.now();
        final LocalDateTime threeMonthsAgo = now.minusMonths(3);

        return financePrepaymentOrderDao.getRecentlyPrepaymentMoney(supplierCode, PrepaymentOrderStatus.DELETED,
                threeMonthsAgo, now);
    }


    public Long getApproveFailTimesByFollowUser(FeishuAuditOrderType feishuAuditOrderType, String followUser,
                                                LocalDateTime startTime, LocalDateTime endTime) {
        return financePrepaymentOrderDao.getApproveFailTimesByFollowUser(feishuAuditOrderType, followUser, startTime, endTime);
    }

    public Long getApproveFailTimesBySupplier(FeishuAuditOrderType feishuAuditOrderType,
                                              String supplierCode, LocalDateTime startTime, LocalDateTime endTime) {
        return financePrepaymentOrderDao.getApproveFailTimesBySupplier(feishuAuditOrderType, supplierCode, startTime, endTime);

    }


    /**
     * 根据供应商与创建时间获取可抵扣的预付款单
     *
     * @param dto
     * @return
     */
    public List<PrepaymentBo> getPrepaymentBoBySupplier(@NotNull @Valid PrepaymentSupplierDto dto) {
        // 状态是全部付款的、未关联的
        final List<FinancePrepaymentOrderPo> financePrepaymentOrderPoList = financePrepaymentOrderDao.getPrepaymentBoBySupplier(dto.getSupplierCode(),
                dto.getCreateTimeStart(), dto.getCreateTimeEnd(), PrepaymentOrderStatus.FULL_PAYMENT, DeductionStatus.UNASSOCIATED);
        if (CollectionUtils.isEmpty(financePrepaymentOrderPoList)) {
            return Collections.emptyList();
        }

        // 创建关联数据
        final List<FinanceRecoPrepaymentPo> financeRecoPrepaymentPoList = financePrepaymentOrderPoList.stream()
                .map(po -> {
                    final FinanceRecoPrepaymentPo financeRecoPrepaymentPo = new FinanceRecoPrepaymentPo();
                    financeRecoPrepaymentPo.setFinanceRecoOrderNo(dto.getFinanceRecoOrderNo());
                    financeRecoPrepaymentPo.setPrepaymentOrderNo(po.getPrepaymentOrderNo());
                    financeRecoPrepaymentPo.setSupplierCode(po.getSupplierCode());
                    financeRecoPrepaymentPo.setDeductionMoney(po.getCanDeductionMoney());
                    return financeRecoPrepaymentPo;
                }).collect(Collectors.toList());
        financeRecoPrepaymentDao.insertBatch(financeRecoPrepaymentPoList);

        // 构建出参
        final List<PrepaymentBo> prepaymentBoList = financePrepaymentOrderPoList.stream().map(po -> {
            PrepaymentBo prepaymentBo = new PrepaymentBo();
            prepaymentBo.setSupplierCode(po.getSupplierCode());
            prepaymentBo.setCanDeductionMoney(po.getCanDeductionMoney());
            prepaymentBo.setPrepaymentOrderNo(po.getPrepaymentOrderNo());
            prepaymentBo.setPrepaymentOrderId(po.getFinancePrepaymentOrderId());
            return prepaymentBo;
        }).collect(Collectors.toList());

        // 更新抵扣状态和抵扣金额
        financePrepaymentOrderPoList.forEach(po -> {
            po.setDeductionStatus(DeductionStatus.ASSOCIATED);
            po.setCanDeductionMoney(BigDecimal.ZERO);
        });
        financePrepaymentOrderDao.updateBatchByIdVersion(financePrepaymentOrderPoList);


        // 返回出参
        return prepaymentBoList;
    }

    /**
     * 对账单取消与预付款单关联后，重置预付款单
     *
     * @param dto
     */
    public void resetPrepayment(@NotNull @Valid PrepaymentNoListDto dto) {
        final List<FinanceRecoPrepaymentPo> financeRecoPrepaymentPoList = financeRecoPrepaymentDao.getListByPrepaymentNoList(dto.getPrepaymentOrderNoList());
        if (CollectionUtils.isEmpty(financeRecoPrepaymentPoList)) {
            throw new BizException("查找不到对应的预付款关联关系，取消关联失败！");
        }
        if (dto.getPrepaymentOrderNoList().size() != financeRecoPrepaymentPoList.size()) {
            throw new BizException("部分预付款单号查找不到对应的预付款关联关系，取消关联失败！");
        }
        final List<FinancePrepaymentOrderPo> financePrepaymentOrderPoList = financePrepaymentOrderDao.getListByNoList(dto.getPrepaymentOrderNoList());
        if (CollectionUtils.isEmpty(financePrepaymentOrderPoList)
                || dto.getPrepaymentOrderNoList().size() != financePrepaymentOrderPoList.size()) {
            throw new BizException("查找不到对应的预付款单数据，取消关联失败！");
        }
        final Map<String, FinanceRecoPrepaymentPo> prepaymentNoPoMap = financeRecoPrepaymentPoList.stream()
                .collect(Collectors.toMap(FinanceRecoPrepaymentPo::getPrepaymentOrderNo, Function.identity()));

        // 更新原预付款数据
        financePrepaymentOrderPoList.forEach(po -> {
            // 更新抵扣状态和抵扣金额
            final FinanceRecoPrepaymentPo financeRecoPrepaymentPo = prepaymentNoPoMap.get(po.getPrepaymentOrderNo());
            if (null == financeRecoPrepaymentPo) {
                throw new BizException("关联关系数据错误，预付款单:{}数据不存在，请联系系统管理员！", po.getPrepaymentOrderNo());
            }
            po.setCanDeductionMoney(financeRecoPrepaymentPo.getDeductionMoney());
            po.setDeductionStatus(DeductionStatus.UNASSOCIATED);
        });
        financePrepaymentOrderDao.updateBatchByIdVersion(financePrepaymentOrderPoList);

        // 删除关联关系
        financeRecoPrepaymentDao.removeBatchByIds(financeRecoPrepaymentPoList);


    }


    /**
     * 飞书审批转交
     *
     * @param prepaymentOrderNoDtoMap
     * @param po
     * @param userVo
     * @param dto
     * @param failPrepaymentCodeList
     */
    @Transactional(rollbackFor = Exception.class)
    public void feiShuTransfer(Map<String, PrepaymentTransferItemDto> prepaymentOrderNoDtoMap,
                               FinancePrepaymentOrderPo po, UserVo userVo, PrepaymentTransferDto dto,
                               List<String> failPrepaymentCodeList) {
        final PrepaymentTransferItemDto itemDto = prepaymentOrderNoDtoMap.get(po.getPrepaymentOrderNo());
        // 推飞书转交
        final WorkflowTransferDto workflowTransferDto = new WorkflowTransferDto();
        workflowTransferDto.setWorkflowNo(po.getWorkflowNo());
        workflowTransferDto.setTaskId(itemDto.getTaskId());
        workflowTransferDto.setComment(itemDto.getComment());
        workflowTransferDto.setTransferUserCode(dto.getTransferUser());
        workflowTransferDto.setUserCode(GlobalContext.getUserKey());
        try {
            mcRemoteService.transferWorkFlow(workflowTransferDto);

            // 日志
            final LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("转交给");
            logVersionBo.setValue("【" + userVo.getUsername() + "】");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                    po.getPrepaymentOrderNo(), po.getPrepaymentOrderStatus().getRemark(),
                    Collections.singletonList(logVersionBo));
        } catch (Exception e) {
            log.error("飞书转交发送失败", e);
            failPrepaymentCodeList.add(po.getPrepaymentOrderNo());
        }
    }


    /**
     * 未发起飞书审批转交，变更操作人
     *
     * @param financePrepaymentOrderPoList
     * @param dto
     * @param userVo
     */
    @Transactional(rollbackFor = Exception.class)
    public void simpleTransfer(List<FinancePrepaymentOrderPo> financePrepaymentOrderPoList, PrepaymentTransferDto dto, UserVo userVo) {
        financePrepaymentOrderPoList.forEach(po -> {
            po.setCtrlUser(dto.getTransferUser());
            // 日志
            final LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("转交给");
            logVersionBo.setValue("【" + userVo.getUsername() + "】");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                    po.getPrepaymentOrderNo(), po.getPrepaymentOrderStatus().getRemark(),
                    Collections.singletonList(logVersionBo));
        });

        financePrepaymentOrderDao.updateBatchByIdVersion(financePrepaymentOrderPoList);
    }
}
