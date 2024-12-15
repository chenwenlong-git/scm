package com.hete.supply.scm.server.scm.ibfs.service.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.workflow.entity.dto.FeiShuWorkflowCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.FinanceSettleOrderApproveBo;
import com.hete.support.id.service.IdGenerateService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/25.
 */
public class IbfsSettlementApproveCreator extends AbstractApproveCreator<FinanceSettleOrderApproveBo> {

    public IbfsSettlementApproveCreator(IdGenerateService idGenerateService,
                                        McRemoteService mcRemoteService,
                                        FeishuAuditOrderDao feishuAuditOrderDao) {
        super(idGenerateService, mcRemoteService, feishuAuditOrderDao);
    }

    @Override
    protected FeishuWorkflowType getFeiShuWorkflowType() {
        return FeishuWorkflowType.IBFS_SETTLEMENT_APPROVE;
    }

    @Override
    protected FeishuAuditOrderType getFeiShuAuditOrderType() {
        return FeishuAuditOrderType.IFBS_SETTLEMENT_APPROVE;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.TextField> getTextFieldList(FinanceSettleOrderApproveBo financeSettleOrderApproveBo) {
        List<FeiShuWorkflowCreateDto.TextField> textFieldList = Lists.newArrayList();

        final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
        textField1.setId("recipientSubject");
        textField1.setType("textarea");
        textField1.setValue(financeSettleOrderApproveBo.getRecipientSubject());
        textFieldList.add(textField1);

        final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
        textField2.setId("cargoCompanyName");
        textField2.setType("textarea");
        textField2.setValue(financeSettleOrderApproveBo.getCargoCompanyName());
        textFieldList.add(textField2);

        final FeiShuWorkflowCreateDto.TextField textField3 = new FeiShuWorkflowCreateDto.TextField();
        textField3.setId("settleAmount");
        textField3.setType("number");
        textField3.setValue(financeSettleOrderApproveBo.getSettleAmount());
        textFieldList.add(textField3);

        final FeiShuWorkflowCreateDto.TextField textField5 = new FeiShuWorkflowCreateDto.TextField();
        textField5.setId("settlementType");
        textField5.setType("textarea");
        textField5.setValue(financeSettleOrderApproveBo.getSettlementType());
        textFieldList.add(textField5);

        final FeiShuWorkflowCreateDto.TextField textField6 = new FeiShuWorkflowCreateDto.TextField();
        textField6.setId("applyDate");
        textField6.setType("textarea");
        textField6.setValue(financeSettleOrderApproveBo.getApplyDate());
        textFieldList.add(textField6);

        final FeiShuWorkflowCreateDto.TextField textField7 = new FeiShuWorkflowCreateDto.TextField();
        textField7.setId("dept");
        textField7.setType("textarea");
        textField7.setValue(financeSettleOrderApproveBo.getDept());
        textFieldList.add(textField7);

        final FeiShuWorkflowCreateDto.TextField textField8 = new FeiShuWorkflowCreateDto.TextField();
        textField8.setId("reason");
        textField8.setType("textarea");
        textField8.setValue(financeSettleOrderApproveBo.getReason());
        textFieldList.add(textField8);

        if (StrUtil.isNotBlank(financeSettleOrderApproveBo.getRemarks())) {
            FeiShuWorkflowCreateDto.TextField textField9 = new FeiShuWorkflowCreateDto.TextField();
            textField9.setId("remarks");
            textField9.setType("textarea");
            textField9.setValue(financeSettleOrderApproveBo.getRemarks());
            textFieldList.add(textField9);
        }

        return textFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiTextField> getMultiTextField(FinanceSettleOrderApproveBo bo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayField> getDayField(FinanceSettleOrderApproveBo financeSettleOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayZoneField> getDayZoneField(FinanceSettleOrderApproveBo financeSettleOrderApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiFileField> getMultiFileField(FinanceSettleOrderApproveBo bo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DetailField> getDetailField(FinanceSettleOrderApproveBo bo) {
        List<List<? super FeiShuWorkflowCreateDto.BaseField>> textFieldListList = new ArrayList<>();

        List<FinanceSettleOrderApproveBo.SettlementDetailApproveBo> detail = bo.getDetail();
        detail.forEach(detailBo -> {
            List<? super FeiShuWorkflowCreateDto.BaseField> textFieldList = new ArrayList<>();

            FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
            textField1.setId("detailType");
            textField1.setType("input");
            textField1.setValue(detailBo.getDetailType());
            textFieldList.add(textField1);

            FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
            textField2.setId("rmbText");
            textField2.setType("input");
            textField2.setValue(detailBo.getRmbText());
            textFieldList.add(textField2);

            FeiShuWorkflowCreateDto.AmountField textField3 = new FeiShuWorkflowCreateDto.AmountField();
            textField3.setId("rmb");
            textField3.setType("amount");
            textField3.setValue(detailBo.getRmb());
            textFieldList.add(textField3);
            textFieldListList.add(textFieldList);
        });

        FeiShuWorkflowCreateDto.DetailField detailField1 = new FeiShuWorkflowCreateDto.DetailField();
        detailField1.setId("detail");
        detailField1.setType("fieldList");
        detailField1.setValue(textFieldListList);

        List<FeiShuWorkflowCreateDto.DetailField> detailFieldList = Lists.newArrayList();
        detailFieldList.add(detailField1);
        return detailFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiContentField> getMultiContentField(FinanceSettleOrderApproveBo bo) {
        List<String> instanceCodes = bo.getInstanceCodes();
        if (CollectionUtils.isNotEmpty(instanceCodes)) {
            FeiShuWorkflowCreateDto.MultiContentField multiContentField = new FeiShuWorkflowCreateDto.MultiContentField();
            multiContentField.setId("association");
            multiContentField.setType("connect");
            multiContentField.setValue(instanceCodes);
            return List.of(multiContentField);
        }
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.NodeApproverUserCode> getNodeApproverUserCodeList(FinanceSettleOrderApproveBo bo) {
        return null;
    }
}
