package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.hete.supply.mc.api.workflow.entity.dto.FeiShuWorkflowCreateDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.FinancePrepaymentApproveBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.PrepaymentDetailApproveBo;
import com.hete.support.id.service.IdGenerateService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/15 16:03
 */
public class IbfsPrepaymentApproveCreator extends AbstractApproveCreator<FinancePrepaymentApproveBo> {
    private final static String SUPPLIER_PREFIX = "货物供应商";

    public IbfsPrepaymentApproveCreator(IdGenerateService idGenerateService,
                                        McRemoteService mcRemoteService,
                                        FeishuAuditOrderDao feishuAuditOrderDao) {
        super(idGenerateService, mcRemoteService, feishuAuditOrderDao);
    }

    @Override
    protected FeishuWorkflowType getFeiShuWorkflowType() {
        return FeishuWorkflowType.IBFS_PREPAYMENT_APPROVE;
    }

    @Override
    protected FeishuAuditOrderType getFeiShuAuditOrderType() {
        return FeishuAuditOrderType.IBFS_PREPAYMENT_APPROVE;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.TextField> getTextFieldList(FinancePrepaymentApproveBo bo) {
        List<FeiShuWorkflowCreateDto.TextField> textFieldList = new ArrayList<>();
        final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField3 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField4 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField5 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField6 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField7 = new FeiShuWorkflowCreateDto.TextField();
        textField1.setId("recipientSubject");
        textField1.setType("textarea");
        textField2.setId("supplierCode");
        textField2.setType("textarea");
        textField3.setId("prepaymentMoney");
        textField3.setType("number");
        textField4.setId("prepaymentType");
        textField4.setType("textarea");
        textField5.setId("applyDate");
        textField5.setType("textarea");
        textField6.setId("dept");
        textField6.setType("textarea");
        textField7.setId("reason");
        textField7.setType("textarea");


        textField1.setValue(bo.getRecipientSubject());
        textField2.setValue(SUPPLIER_PREFIX + bo.getSupplierCode());
        textField3.setValue(bo.getPrepaymentMoney());
        textField4.setValue(bo.getPrepaymentType());
        textField5.setValue(bo.getApplyDate());
        textField6.setValue(bo.getDept());
        textField7.setValue(bo.getReason());

        textFieldList.add(textField1);
        textFieldList.add(textField2);
        textFieldList.add(textField3);
        textFieldList.add(textField4);
        textFieldList.add(textField5);
        textFieldList.add(textField6);
        textFieldList.add(textField7);

        return textFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiTextField> getMultiTextField(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayField> getDayField(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayZoneField> getDayZoneField(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiFileField> getMultiFileField(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        List<String> fileCodeList = financePrepaymentApproveBo.getFileCodeList();
        if (CollectionUtils.isNotEmpty(fileCodeList)) {
            FeiShuWorkflowCreateDto.MultiFileField fileField = new FeiShuWorkflowCreateDto.MultiFileField();
            fileField.setId("fileCodeList");
            fileField.setType(ScmConstant.WORKFLOW_TYPE_ATTACHMENT);
            fileField.setValue(financePrepaymentApproveBo.getFileCodeList());
            return List.of(fileField);
        }
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DetailField> getDetailField(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        final List<PrepaymentDetailApproveBo> prepaymentDetailApproveList = financePrepaymentApproveBo.getPrepaymentDetailApproveList();
        if (CollectionUtils.isEmpty(prepaymentDetailApproveList)) {
            return Collections.emptyList();
        }

        List<FeiShuWorkflowCreateDto.DetailField> detailFieldList = new ArrayList<>();
        final FeiShuWorkflowCreateDto.DetailField detailField = new FeiShuWorkflowCreateDto.DetailField();
        List<List<? super FeiShuWorkflowCreateDto.BaseField>> textFieldListList = new ArrayList<>();
        detailField.setId("detail");
        detailField.setType("fieldList");

        prepaymentDetailApproveList.forEach(detailBo -> {
            List<? super FeiShuWorkflowCreateDto.BaseField> textFieldList = new ArrayList<>();
            final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
            final FeiShuWorkflowCreateDto.AmountField textField2 = new FeiShuWorkflowCreateDto.AmountField();
            final FeiShuWorkflowCreateDto.TextField textField3 = new FeiShuWorkflowCreateDto.TextField();

            textField1.setId("detailType");
            textField1.setType("input");
            textField2.setId("rmb");
            textField2.setType("amount");
            textField3.setId("rmbText");
            textField3.setType("input");

            textField1.setValue(detailBo.getDetailType());
            textField2.setValue(detailBo.getPrepaymentDetailMoney());
            textField2.setCurrency(detailBo.getCurrency().getCurrency());
            textField3.setValue(detailBo.getRmbText());

            textFieldList.add(textField1);
            textFieldList.add(textField2);
            textFieldList.add(textField3);
            textFieldListList.add(textFieldList);
        });

        detailField.setValue(textFieldListList);
        detailFieldList.add(detailField);
        return detailFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiContentField> getMultiContentField(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.NodeApproverUserCode> getNodeApproverUserCodeList(FinancePrepaymentApproveBo financePrepaymentApproveBo) {
        return null;
    }
}
