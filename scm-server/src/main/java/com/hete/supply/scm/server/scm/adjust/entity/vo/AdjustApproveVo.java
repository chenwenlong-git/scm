package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.supply.scm.server.scm.adjust.enums.ApproveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/6/13 14:39
 */
@Data
@NoArgsConstructor
public class AdjustApproveVo {
    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;

    @ApiModelProperty(value = "审批类型")
    private ApproveType approveType;

    @ApiModelProperty(value = "审批状态")
    private ApproveStatus approveStatus;

    @ApiModelProperty(value = "申请人")
    private String applyUser;

    @ApiModelProperty(value = "申请人")
    private String applyUsername;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "当前审批人")
    private String approveUser;

    @ApiModelProperty(value = "当前审批人")
    private String approveUsername;

    @ApiModelProperty(value = "审批人头像")
    private String avatar;

    @ApiModelProperty(value = "飞书审批单号")
    private String workflowNo;


    @ApiModelProperty(value = "审批taskId")
    private String taskId;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

}
