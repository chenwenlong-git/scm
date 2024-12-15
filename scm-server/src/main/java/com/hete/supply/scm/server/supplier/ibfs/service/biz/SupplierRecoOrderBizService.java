package com.hete.supply.scm.server.supplier.ibfs.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoOrderDao;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.SupplierConfirmRecoOrderDto;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/14 17:02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierRecoOrderBizService {

    private final FinanceRecoOrderDao financeRecoOrderDao;
    private final LogBaseService logBaseService;
    private final AuthBaseService authBaseService;
    private final ConsistencySendMqService consistencySendMqService;

    /**
     * 工厂确认
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 17:37
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitRecoOrder(SupplierConfirmRecoOrderDto dto) {
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(dto.getFinanceRecoOrderNo(), dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        // 当前角色拥有的供应商权限
        final List<String> authSupplierCodeList = authBaseService.getSupplierCodeList();
        if (!FinanceRecoOrderStatus.WAIT_SUPPLIER_CONFIRM.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态待工厂确认，无法进行此操作，请刷新页面后重试！");
        }
        if (!authSupplierCodeList.contains(financeRecoOrderPo.getSupplierCode())) {
            throw new ParamIllegalException("当前登录账号无对账单{}的数据权限，无法进行此操作！", financeRecoOrderPo.getFinanceRecoOrderNo());
        }

        // 更新数据
        FinanceRecoOrderStatus financeRecoOrderStatus;
        String logRemark = "拒绝";
        if (BooleanType.TRUE.equals(dto.getExamine())) {
            financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus().toWaitFollowConfirm();
            logRemark = "通过";
        } else {
            if (StringUtils.isBlank(dto.getComment())) {
                throw new ParamIllegalException("拒绝时工厂确认意见不能为空，请填写工厂确认意见！", financeRecoOrderPo.getFinanceRecoOrderNo());
            }
            financeRecoOrderPo.setComment(dto.getComment());
            financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus().toRejectWaitSubmit();
        }

        financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);
        financeRecoOrderPo.setSupplierConfirmTime(LocalDateTime.now());
        financeRecoOrderPo.setCtrlUser(financeRecoOrderPo.getFollowUser());
        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("工厂确认-" + logRemark);
        logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                Collections.singletonList(logVersionBo));

    }

    /**
     * 对账单导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/20 11:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportRecoOrder(RecoOrderSearchDto dto) {
        Integer exportTotals = financeRecoOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SPM_FINANCE_RECO_ORDER_EXPORT.getCode(), dto));
    }


    /**
     * 对账单详情导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/21 11:04
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportRecoOrderItem(RecoOrderSearchDto dto) {
        Integer exportTotals = financeRecoOrderDao.getExportTotalsItem(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SPM_FINANCE_RECO_ORDER_ITEM_EXPORT.getCode(), dto));
    }

}
