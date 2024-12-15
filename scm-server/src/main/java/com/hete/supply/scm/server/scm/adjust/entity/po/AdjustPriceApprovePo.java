package com.hete.supply.scm.server.scm.adjust.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 调价审批表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("adjust_price_approve")
@ApiModel(value = "AdjustPriceApprovePo对象", description = "调价审批表")
public class AdjustPriceApprovePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "adjust_price_approve_id", type = IdType.ASSIGN_ID)
    private Long adjustPriceApproveId;


    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;


    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;


    @ApiModelProperty(value = "审批状态")
    private ApproveStatus approveStatus;


    @ApiModelProperty(value = "审批类型")
    private ApproveType approveType;


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


    @ApiModelProperty(value = "飞书审批单号")
    private String workflowNo;


    @ApiModelProperty(value = "审批taskId")
    private String taskId;


}
