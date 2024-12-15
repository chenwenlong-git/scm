package com.hete.supply.scm.remote.dubbo;

import com.hete.supply.mc.api.feishu.entity.vo.FeiShuAccessTokenVo;
import com.hete.supply.mc.api.feishu.facade.FeiShuTokenFacade;
import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesBatchCreateDto;
import com.hete.supply.mc.api.tables.entity.vo.FeiShuTablesRecordVo;
import com.hete.supply.mc.api.tables.facade.TablesFacade;
import com.hete.supply.mc.api.workflow.entity.dto.*;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowCreateVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowDetailVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowInstanceCodeVo;
import com.hete.supply.mc.api.workflow.facade.WorkflowFacade;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/3/12 20:45
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class McRemoteService {
    @DubboReference(check = false)
    private FeiShuTokenFacade feiShuTokenFacade;

    @DubboReference(check = false)
    private TablesFacade tablesFacade;

    @DubboReference(check = false)
    private WorkflowFacade workflowFacade;

    /**
     * 获取飞书审批详情
     *
     * @param workflowNo
     */
    public WorkflowDetailVo workFlowDetail(String workflowNo) {
        if (StringUtils.isBlank(workflowNo)) {
            return null;
        }
        final WorkflowNoCodeDto workflowNoCodeDto = new WorkflowNoCodeDto();
        workflowNoCodeDto.setWorkflowNo(workflowNo);
        final CommonResult<WorkflowDetailVo> result = workflowFacade.workFlowDetail(workflowNoCodeDto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }


    /**
     * 创建飞书审批
     *
     * @param dto
     */
    public void createFeiShuInstance(FeiShuWorkflowCreateDto dto) {
        final CommonResult<WorkflowCreateVo> commonResult = workflowFacade.createFeiShuInstance(dto);
        DubboResponseUtil.checkCodeAndGetData(commonResult);
    }


    public FeiShuTablesRecordVo batchCreateRecord(@NotBlank String appToken,
                                                  @NotBlank String tableId,
                                                  FeiShuTablesBatchCreateDto dto) {
        CommonResult<FeiShuTablesRecordVo> commonResult = tablesFacade.feiShuTablesBatchCreate(appToken, tableId, dto);
        return DubboResponseUtil.checkCodeAndGetData(commonResult);
    }

    /**
     * MC获取飞书token
     *
     * @return
     */
    public FeiShuAccessTokenVo getFeiShuToken() {
        CommonResult<FeiShuAccessTokenVo> commonResult = feiShuTokenFacade.getAccessToken();
        return DubboResponseUtil.checkCodeAndGetData(commonResult);
    }


    /**
     * 同意
     *
     * @param dto
     */
    public void approveWorkFlow(WorkflowApproveDto dto) {
        final CommonResult<Void> commonResult = workflowFacade.approveWorkFlow(dto);
        DubboResponseUtil.checkCodeAndGetData(commonResult);
    }

    /**
     * 拒绝
     *
     * @param dto
     */
    public void rejectWorkFlow(WorkflowApproveDto dto) {
        final CommonResult<Void> commonResult = workflowFacade.rejectWorkFlow(dto);
        DubboResponseUtil.checkCodeAndGetData(commonResult);
    }

    /**
     * 转交
     *
     * @param dto
     */
    public void transferWorkFlow(WorkflowTransferDto dto) {
        final CommonResult<Void> commonResult = workflowFacade.transferWorkFlow(dto);
        DubboResponseUtil.checkCodeAndGetData(commonResult);
    }


    /**
     * 根据workFlowNo查询instanceCode
     *
     * @param dto
     */
    public List<WorkflowInstanceCodeVo> getInstanceCodeByWorkflowNoList(WorkflowNoListDto dto) {
        CommonResult<ResultList<WorkflowInstanceCodeVo>> instanceCodeByWorkflowNoList
                = workflowFacade.getInstanceCodeByWorkflowNoList(dto);
        ResultList<WorkflowInstanceCodeVo> workflowInstanceCodeVoResultList = DubboResponseUtil.checkCodeAndGetData(
                instanceCodeByWorkflowNoList);
        return workflowInstanceCodeVoResultList.getList();
    }


}
