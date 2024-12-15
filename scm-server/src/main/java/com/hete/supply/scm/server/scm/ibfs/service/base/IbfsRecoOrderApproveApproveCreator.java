package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.mc.api.workflow.entity.dto.FeiShuWorkflowCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderApproveBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderDetailApproveBo;
import com.hete.support.id.service.IdGenerateService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/24 14:49
 */
public class IbfsRecoOrderApproveApproveCreator extends AbstractApproveCreator<RecoOrderApproveBo> {

    public IbfsRecoOrderApproveApproveCreator(IdGenerateService idGenerateService,
                                              McRemoteService mcRemoteService,
                                              FeishuAuditOrderDao feishuAuditOrderDao) {
        super(idGenerateService, mcRemoteService, feishuAuditOrderDao);
    }

    @Override
    protected FeishuWorkflowType getFeiShuWorkflowType() {
        return FeishuWorkflowType.IBFS_RECO_ORDER_APPROVE;
    }

    @Override
    protected FeishuAuditOrderType getFeiShuAuditOrderType() {
        return FeishuAuditOrderType.IFBS_RECO_ORDER_APPROVE;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.TextField> getTextFieldList(RecoOrderApproveBo recoOrderApproveBo) {
        List<FeiShuWorkflowCreateDto.TextField> textFieldList = new ArrayList<>();
        final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField4 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField5 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField6 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField7 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField8 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField9 = new FeiShuWorkflowCreateDto.TextField();

        textField1.setId("supplierCode");
        textField1.setType("textarea");
        textField1.setValue(recoOrderApproveBo.getSupplierCode());
        textFieldList.add(textField1);

        textField2.setId("settlePrice");
        textField2.setType("number");
        textField2.setValue(recoOrderApproveBo.getSettlePrice());
        textFieldList.add(textField2);

        textField4.setId("reconciliationCycleTime");
        textField4.setType("textarea");
        textField4.setValue(recoOrderApproveBo.getReconciliationCycleTime());
        textFieldList.add(textField4);

        textField5.setId("reconciliationCycleDay");
        textField5.setType("textarea");
        textField5.setValue(recoOrderApproveBo.getReconciliationCycleDay());
        textFieldList.add(textField5);

        textField6.setId("reconciliationCycleValue");
        textField6.setType("textarea");
        textField6.setValue(recoOrderApproveBo.getReconciliationCycleValue());
        textFieldList.add(textField6);

        textField7.setId("reason");
        textField7.setType("textarea");
        textField7.setValue(recoOrderApproveBo.getReason());
        textFieldList.add(textField7);

        textField8.setId("dept");
        textField8.setType("textarea");
        textField8.setValue(recoOrderApproveBo.getDept());
        textFieldList.add(textField8);

        if (StringUtils.isNotBlank(recoOrderApproveBo.getRemarks())) {
            textField9.setId("remarks");
            textField9.setType("textarea");
            textField9.setValue(recoOrderApproveBo.getRemarks());
            textFieldList.add(textField9);
        }


        return textFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiTextField> getMultiTextField(RecoOrderApproveBo recoOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayField> getDayField(RecoOrderApproveBo recoOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayZoneField> getDayZoneField(RecoOrderApproveBo recoOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiFileField> getMultiFileField(RecoOrderApproveBo recoOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DetailField> getDetailField(RecoOrderApproveBo recoOrderApproveBo) {
        List<RecoOrderDetailApproveBo> recoOrderDetailApproveList = recoOrderApproveBo.getRecoOrderDetailApproveList();
        if (CollectionUtils.isEmpty(recoOrderDetailApproveList)) {
            return Collections.emptyList();
        }

        List<FeiShuWorkflowCreateDto.DetailField> detailFieldList = new ArrayList<>();
        final FeiShuWorkflowCreateDto.DetailField detailField = new FeiShuWorkflowCreateDto.DetailField();
        List<List<? super FeiShuWorkflowCreateDto.BaseField>> textFieldListList = new ArrayList<>();
        detailField.setId("detail");
        detailField.setType("fieldList");

        recoOrderDetailApproveList.forEach(detailBo -> {
            List<? super FeiShuWorkflowCreateDto.BaseField> textFieldList = new ArrayList<>();
            final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
            final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
            final FeiShuWorkflowCreateDto.AmountField textField3 = new FeiShuWorkflowCreateDto.AmountField();

            textField1.setId("detailType");
            textField1.setType("input");
            textField2.setId("rmbText");
            textField2.setType("input");
            textField3.setId("rmb");
            textField3.setType("amount");


            textField1.setValue(detailBo.getDetailType());
            textField2.setValue(detailBo.getRmbText());
            textField3.setValue(detailBo.getRmb());

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
    protected List<FeiShuWorkflowCreateDto.MultiContentField> getMultiContentField(RecoOrderApproveBo recoOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.NodeApproverUserCode> getNodeApproverUserCodeList(RecoOrderApproveBo recoOrderApproveBo) {
        return null;
    }
}
