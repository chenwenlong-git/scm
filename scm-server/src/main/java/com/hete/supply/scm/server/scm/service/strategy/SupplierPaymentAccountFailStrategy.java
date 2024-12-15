package com.hete.supply.scm.server.scm.service.strategy;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.handler.WorkflowMqBackStrategy;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierPaymentAccountDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierPaymentAccountBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hete.supply.mc.api.workflow.enums.WorkflowResult.AGREE;
import static com.hete.supply.mc.api.workflow.enums.WorkflowResult.REFUSE;

/**
 * 审批单失效逻辑处理策略
 *
 * @author ChenWenLong
 * @date 2023/12/14 11:46
 */
@Service
@RequiredArgsConstructor
public class SupplierPaymentAccountFailStrategy implements WorkflowMqBackStrategy {

    private final SupplierPaymentAccountDao supplierPaymentAccountDao;
    private final SupplierPaymentAccountBaseService supplierPaymentAccountBaseService;

    @Override
    public void workflowMqBackHandleBusiness(WorkflowMqCallbackDto dto, FeishuAuditOrderPo feishuAuditOrderPo) {
        SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountDao.getByFeishuAuditOrderNo(feishuAuditOrderPo.getFeishuAuditOrderNo());
        if (supplierPaymentAccountPo != null) {
            if (AGREE.equals(dto.getWorkflowResult())) {
                supplierPaymentAccountPo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.INVALID);
                supplierPaymentAccountDao.updateByIdVersion(supplierPaymentAccountPo);
                // 日志
                supplierPaymentAccountBaseService.createStatusChangeLog(supplierPaymentAccountPo, "失效审批通过", null);
            }
            if (REFUSE.equals(dto.getWorkflowResult())) {
                supplierPaymentAccountPo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.REFUSED);
                supplierPaymentAccountDao.updateByIdVersion(supplierPaymentAccountPo);
                // 日志
                supplierPaymentAccountBaseService.createStatusChangeLog(supplierPaymentAccountPo, "失效审批不通过", null);
            }
            // 撤销、失败状态处理
            if (WorkflowState.FAIL.equals(dto.getWorkflowState())
                    || WorkflowState.TERMINATE.equals(dto.getWorkflowState())) {
                supplierPaymentAccountPo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.CREATE_FAIL);
                supplierPaymentAccountDao.updateByIdVersion(supplierPaymentAccountPo);
                // 日志
                supplierPaymentAccountBaseService.createStatusChangeLog(supplierPaymentAccountPo, "创建审批单失败", null);
            }
        }
    }

    @Override
    public FeishuWorkflowType getHandlerType() {
        return FeishuWorkflowType.SUPPLIER_PAYMENT_ACCOUNT_FAIL;
    }
}
