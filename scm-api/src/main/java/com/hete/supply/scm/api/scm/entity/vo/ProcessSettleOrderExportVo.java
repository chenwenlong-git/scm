package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ProcessSettleOrderExportVo {

    @ApiModelProperty(value = "结算单号")
    private String processSettleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private ProcessSettleStatus processSettleStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "扫码完成人")
    private String completeUsername;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "加工单数")
    private Integer processNum;

    @ApiModelProperty(value = "加工产品数（正品）")
    private Integer skuNum;

    @ApiModelProperty(value = "核算时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "核算人")
    private String examineUsername;

    @ApiModelProperty(value = "审核人")
    private String settleUsername;

    @ApiModelProperty(value = "结算审核时间")
    private LocalDateTime settleTime;


}
