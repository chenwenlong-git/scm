package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/8/1 14:56
 */
@Data
@NoArgsConstructor
public class DevelopSampleSettleSearchVo {

    @ApiModelProperty(value = "id")
    private Long developSampleSettleOrderId;

    @ApiModelProperty(value = "结算单号")
    private String developSampleSettleOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "结算单状态")
    private DevelopSampleSettleStatus developSampleSettleStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "财务审核时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "已支付金额")
    private BigDecimal alreadyPayPrice;

    @ApiModelProperty(value = "待支付金额")
    private BigDecimal waitPayPrice;

    @ApiModelProperty(value = "单据总数")
    private Long itemTotal;

    @ApiModelProperty(value = "对账人的用户名")
    private String confirmUsername;

    @ApiModelProperty(value = "供应商确认人的用户名")
    private String examineUsername;

    @ApiModelProperty(value = "财务审核人的用户名")
    private String settleUsername;

    @ApiModelProperty(value = "供应商拒绝原因")
    private String settleRefuseRemarks;


}
