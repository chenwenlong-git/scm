package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class ProcessSettleOrderVo {

    @ApiModelProperty(value = "加工结算单ID")
    private Long processSettleOrderId;

    @ApiModelProperty(value = "结算单号")
    private String processSettleOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "结算单状态")
    private ProcessSettleStatus processSettleStatus;

    @ApiModelProperty(value = "结算人次")
    private Integer settlePeopleNum;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "核算人")
    private String examineUser;

    @ApiModelProperty(value = "核算人名称")
    private String examineUsername;

    @ApiModelProperty(value = "核算时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "审核人")
    private String settleUser;

    @ApiModelProperty(value = "审核人名称")
    private String settleUsername;

    @ApiModelProperty(value = "结算审核时间")
    private LocalDateTime settleTime;


}
