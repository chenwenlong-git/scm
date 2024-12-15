package com.hete.supply.scm.server.scm.feishu.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 飞书审批单
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("feishu_audit_order")
@ApiModel(value = "FeishuAuditOrderPo对象", description = "飞书审批单")
public class FeishuAuditOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "id")
    @TableId(value = "feishu_audit_order_id", type = IdType.ASSIGN_ID)
    private Long feishuAuditOrderId;


    @ApiModelProperty(value = "编号")
    private String feishuAuditOrderNo;


    @ApiModelProperty(value = "业务ID")
    private Long businessId;


    @ApiModelProperty(value = "业务类型")
    private FeishuAuditOrderType feishuAuditOrderType;


    @ApiModelProperty(value = "工作流单号")
    private String workflowNo;


    @ApiModelProperty(value = "工作流标题")
    private String workflowTitle;


    @ApiModelProperty(value = "工作流状态")
    private WorkflowState workflowState;


    @ApiModelProperty(value = "工作流结果：AGREE(通过),REFUSE(拒绝)")
    private WorkflowResult workflowResult;


    @ApiModelProperty(value = "工作流类型字符串")
    private String workflowType;


    @ApiModelProperty(value = "审批编号")
    private String processBusinessId;


    @ApiModelProperty(value = "发起人")
    private String originatorUser;


    @ApiModelProperty(value = "完成审核时间")
    private LocalDateTime finishTime;


    @ApiModelProperty(value = "审批发起失败信息")
    private String failMsg;


    @ApiModelProperty(value = "最后一次备注（包含审批通过、失败原因)")
    private String latestRemark;

    @ApiModelProperty(value = "最后一次操作人")
    private String latestOperateUser;

    @ApiModelProperty(value = "最后一次操作人")
    private String latestOperateUsername;


}
