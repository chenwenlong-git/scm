package com.hete.supply.scm.server.scm.feishu.service.base;

import com.hete.supply.mc.api.workflow.entity.dto.FeiShuWorkflowCreateDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/15 15:41
 */
public abstract class AbstractApproveCreator<T> {
    protected final IdGenerateService idGenerateService;
    protected final McRemoteService mcRemoteService;
    protected final FeishuAuditOrderDao feishuAuditOrderDao;

    public AbstractApproveCreator(IdGenerateService idGenerateService,
                                  McRemoteService mcRemoteService,
                                  FeishuAuditOrderDao feishuAuditOrderDao) {
        this.idGenerateService = idGenerateService;
        this.mcRemoteService = mcRemoteService;
        this.feishuAuditOrderDao = feishuAuditOrderDao;
    }


    public final void createFeiShuInstance(Long businessId, T t) {
        // 创建scm的飞书审批对象
        final FeishuAuditOrderPo feiShuAuditPo = createFeiShuAuditPo(businessId);

        // 创建业务dto对象
        createApproveForWorkFlow(feiShuAuditPo.getFeishuAuditOrderNo(), t);
    }

    protected abstract FeishuWorkflowType getFeiShuWorkflowType();

    protected abstract FeishuAuditOrderType getFeiShuAuditOrderType();

    protected abstract List<FeiShuWorkflowCreateDto.TextField> getTextFieldList(T t);

    protected abstract List<FeiShuWorkflowCreateDto.MultiTextField> getMultiTextField(T t);

    protected abstract List<FeiShuWorkflowCreateDto.DayField> getDayField(T t);

    protected abstract List<FeiShuWorkflowCreateDto.DayZoneField> getDayZoneField(T t);

    protected abstract List<FeiShuWorkflowCreateDto.MultiFileField> getMultiFileField(T t);

    protected abstract List<FeiShuWorkflowCreateDto.DetailField> getDetailField(T t);

    protected abstract List<FeiShuWorkflowCreateDto.MultiContentField> getMultiContentField(T t);

    protected abstract List<FeiShuWorkflowCreateDto.NodeApproverUserCode> getNodeApproverUserCodeList(T t);

    public List<FeiShuWorkflowCreateDto.AmountField> getAmountFieldList(T t) {
        return null;
    }


    private FeishuAuditOrderPo createFeiShuAuditPo(Long businessId) {
        String feiShuAuditOrderNo = idGenerateService.getConfuseCode(ScmConstant.FEISHU_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        FeishuAuditOrderPo feishuAuditOrderPo = new FeishuAuditOrderPo();
        feishuAuditOrderPo.setFeishuAuditOrderNo(feiShuAuditOrderNo);
        feishuAuditOrderPo.setBusinessId(businessId);
        feishuAuditOrderPo.setFeishuAuditOrderType(getFeiShuAuditOrderType());
        feishuAuditOrderDao.insert(feishuAuditOrderPo);
        return feishuAuditOrderPo;
    }

    private void createApproveForWorkFlow(String workflowReqNo, T t) {
        final FeiShuWorkflowCreateDto feiShuWorkflowCreateDto = new FeiShuWorkflowCreateDto();
        feiShuWorkflowCreateDto.setWorkflowType(getFeiShuWorkflowType().getValue());
        feiShuWorkflowCreateDto.setWorkflowReqNo(workflowReqNo);
        feiShuWorkflowCreateDto.setOriginatorUser(GlobalContext.getUserKey());
        feiShuWorkflowCreateDto.setTextFieldList(getTextFieldList(t));
        feiShuWorkflowCreateDto.setMultiTextFieldList(getMultiTextField(t));
        feiShuWorkflowCreateDto.setDayFieldList(getDayField(t));
        feiShuWorkflowCreateDto.setDayZoneFieldList(getDayZoneField(t));
        feiShuWorkflowCreateDto.setMultiFileFieldList(getMultiFileField(t));
        feiShuWorkflowCreateDto.setDetailFieldList(getDetailField(t));
        feiShuWorkflowCreateDto.setMultiContentFields(getMultiContentField(t));
        feiShuWorkflowCreateDto.setNodeApproverUserCodeList(getNodeApproverUserCodeList(t));
        feiShuWorkflowCreateDto.setAmountFieldList(getAmountFieldList(t));

        mcRemoteService.createFeiShuInstance(feiShuWorkflowCreateDto);
    }


}
