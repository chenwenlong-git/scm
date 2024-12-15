package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProcessSettleOrderDto extends ComPageDto {

    @ApiModelProperty(value = "结算ID")
    private Long processSettleOrderId;

    @ApiModelProperty(value = "结算单IDS")
    private List<Long> processSettleOrderIds;

    @ApiModelProperty(value = "结算单号")
    private String processSettleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private List<ProcessSettleStatus> processSettleStatusList;

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;

    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

    @ApiModelProperty(value = "核算人")
    private String examineUser;

    @ApiModelProperty(value = "核算人名称")
    private String examineUsername;

    @ApiModelProperty(value = "结算审核人")
    private String settleUser;

    @ApiModelProperty(value = "结算审核人名称")
    private String settleUsername;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "核算时间开始")
    private LocalDateTime examineTimeStart;

    @ApiModelProperty(value = "核算时间结束")
    private LocalDateTime examineTimeEnd;

    @ApiModelProperty(value = "结算审核时间开始")
    private LocalDateTime settleTimeStart;

    @ApiModelProperty(value = "结算审核时间结束")
    private LocalDateTime settleTimeEnd;


}
