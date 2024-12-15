package com.hete.supply.scm.remote.dubbo;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.mc.api.workflow.entity.dto.FeiShuWorkflowCreateDto;
import com.hete.supply.mc.api.workflow.facade.WorkflowFacade;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/12/8 10:43
 */
@Component
@RequiredArgsConstructor
public class WorkflowService {
    @DubboReference(check = false)
    private WorkflowFacade workflowFacade;

    /**
     * 创建供应商收款账号飞书生效审批单
     *
     * @param feishuAuditOrderPo:
     * @param supplierPaymentAccountPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/11 14:18
     */
    public void createSupplierPaymentAccountInstance(FeishuAuditOrderPo feishuAuditOrderPo,
                                                     SupplierPaymentAccountPo supplierPaymentAccountPo,
                                                     List<String> personalFileCodeList,
                                                     List<String> authFileCodeList,
                                                     List<String> companyFileCodeList,
                                                     String submitReviewUserKey) {

        List<FeiShuWorkflowCreateDto.TextField> textFields = new ArrayList<>();
        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getAccount())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_ACCOUNT);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getAccount());
            textFields.add(textField);
        }
        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getSupplierCode())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_SUPPLIER_CODE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSupplierCode());
            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getBankName())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_BANK_NAME);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getBankName());
            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getBankSubbranchName())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_BANK_SUBBRANCH_NAME);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getBankSubbranchName());
            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getBankCity())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_BANK_AREA);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getBankProvince() + supplierPaymentAccountPo.getBankCity() + supplierPaymentAccountPo.getBankArea());
            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getAccountUsername())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_ACCOUNT_USERNAME);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getAccountUsername());
            textFields.add(textField);
        }

        if (supplierPaymentAccountPo.getSupplierPaymentAccountType() != null) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_TYPE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSupplierPaymentAccountType().getRemark());
            textFields.add(textField);
        }

        if (supplierPaymentAccountPo.getSupplierPaymentCurrencyType() != null) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_CURRENCY_TYPE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSupplierPaymentCurrencyType().getRemark());
            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getSwiftCode())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_SWIFT_CODE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSwiftCode());
            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getRemarks())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_REMARKS);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getRemarks());
            textFields.add(textField);
        }
        final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
        textField.setId(ScmConstant.WORKFLOW_PAYMENT_APPLICANT);
        textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
        textField.setValue(supplierPaymentAccountPo.getCreateUsername());
        textFields.add(textField);

        final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
        textField1.setId(ScmConstant.WORKFLOW_PAYMENT_APPLICATION_TIME);
        textField1.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
        textField1.setValue(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd"));
        textFields.add(textField1);


        List<FeiShuWorkflowCreateDto.MultiFileField> multiFileFieldList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(companyFileCodeList)) {
            final FeiShuWorkflowCreateDto.MultiFileField multiFileField = new FeiShuWorkflowCreateDto.MultiFileField();
            multiFileField.setId(ScmConstant.WORKFLOW_PAYMENT_COMPANY_FILE_CODE);
            multiFileField.setType(ScmConstant.WORKFLOW_TYPE_ATTACHMENT);
            multiFileField.setValue(companyFileCodeList);
            multiFileFieldList.add(multiFileField);
        }

        if (CollectionUtils.isNotEmpty(personalFileCodeList)) {
            final FeiShuWorkflowCreateDto.MultiFileField multiFileField = new FeiShuWorkflowCreateDto.MultiFileField();
            multiFileField.setId(ScmConstant.WORKFLOW_PAYMENT_PERSONAL_FILE_CODE);
            multiFileField.setType(ScmConstant.WORKFLOW_TYPE_ATTACHMENT);
            multiFileField.setValue(personalFileCodeList);
            multiFileFieldList.add(multiFileField);
        }

        if (CollectionUtils.isNotEmpty(authFileCodeList)) {
            final FeiShuWorkflowCreateDto.MultiFileField multiFileField = new FeiShuWorkflowCreateDto.MultiFileField();
            multiFileField.setId(ScmConstant.WORKFLOW_PAYMENT_COMPANY_AUTH_FILE_CODE);
            multiFileField.setType(ScmConstant.WORKFLOW_TYPE_ATTACHMENT);
            multiFileField.setValue(authFileCodeList);
            multiFileFieldList.add(multiFileField);
        }

        // 供应商主体信息
        String subject = supplierPaymentAccountPo.getSubject();
        if (StrUtil.isNotBlank(subject)) {
            final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
            textField2.setId(ScmConstant.SUPPLIER_SUBJECT);
            textField2.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField2.setValue(subject);
            textFields.add(textField2);
        }

        FeiShuWorkflowCreateDto workflowCreateDto = new FeiShuWorkflowCreateDto();
        workflowCreateDto.setWorkflowType(FeishuWorkflowType.SUPPLIER_PAYMENT_ACCOUNT.getValue());
        workflowCreateDto.setWorkflowReqNo(feishuAuditOrderPo.getFeishuAuditOrderNo());
        workflowCreateDto.setOriginatorUser(submitReviewUserKey);
        workflowCreateDto.setTextFieldList(textFields);
        workflowCreateDto.setMultiFileFieldList(multiFileFieldList);


        DubboResponseUtil.checkCodeAndGetData(workflowFacade.createFeiShuInstance(workflowCreateDto));
    }


    /**
     * 创建供应商收款账号飞书失效审批单
     *
     * @param feishuAuditOrderPo:
     * @param supplierPaymentAccountPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/11 14:18
     */
    public void createSupplierPaymentAccountInvalidInstance(FeishuAuditOrderPo feishuAuditOrderPo,
                                                            SupplierPaymentAccountPo supplierPaymentAccountPo,
                                                            String submitReviewUserKey) {
        List<FeiShuWorkflowCreateDto.TextField> textFields = new ArrayList<>();
        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getAccount())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_ACCOUNT);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getAccount());

            textFields.add(textField);
        }
        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getSupplierCode())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_SUPPLIER_CODE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSupplierCode());

            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getBankName())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_BANK_NAME);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getBankName());

            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getBankSubbranchName())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_BANK_SUBBRANCH_NAME);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getBankSubbranchName());

            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getBankCity())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_BANK_AREA);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getBankProvince() + supplierPaymentAccountPo.getBankCity() + supplierPaymentAccountPo.getBankArea());

            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getAccountUsername())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_ACCOUNT_USERNAME);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getAccountUsername());

            textFields.add(textField);
        }

        if (supplierPaymentAccountPo.getSupplierPaymentAccountType() != null) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_TYPE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSupplierPaymentAccountType().getRemark());

            textFields.add(textField);
        }

        if (supplierPaymentAccountPo.getSupplierPaymentCurrencyType() != null) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_CURRENCY_TYPE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSupplierPaymentCurrencyType().getRemark());

            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getSwiftCode())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_SWIFT_CODE);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getSwiftCode());

            textFields.add(textField);
        }

        if (StringUtils.isNotBlank(supplierPaymentAccountPo.getRemarks())) {
            final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
            textField.setId(ScmConstant.WORKFLOW_PAYMENT_REMARKS);
            textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
            textField.setValue(supplierPaymentAccountPo.getRemarks());

            textFields.add(textField);
        }
        final FeiShuWorkflowCreateDto.TextField textField = new FeiShuWorkflowCreateDto.TextField();
        textField.setId(ScmConstant.WORKFLOW_PAYMENT_APPLICANT);
        textField.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
        textField.setValue(supplierPaymentAccountPo.getCreateUsername());
        textFields.add(textField);

        final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
        textField1.setId(ScmConstant.WORKFLOW_PAYMENT_APPLICATION_TIME);
        textField1.setType(ScmConstant.WORKFLOW_TYPE_TEXT);
        textField1.setValue(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd"));
        textFields.add(textField1);

        FeiShuWorkflowCreateDto workflowCreateDto = new FeiShuWorkflowCreateDto();
        workflowCreateDto.setWorkflowType(FeishuWorkflowType.SUPPLIER_PAYMENT_ACCOUNT_FAIL.getValue());
        workflowCreateDto.setWorkflowReqNo(feishuAuditOrderPo.getFeishuAuditOrderNo());
        workflowCreateDto.setOriginatorUser(submitReviewUserKey);
        workflowCreateDto.setTextFieldList(textFields);

        DubboResponseUtil.checkCodeAndGetData(workflowFacade.createFeiShuInstance(workflowCreateDto));
    }

}
