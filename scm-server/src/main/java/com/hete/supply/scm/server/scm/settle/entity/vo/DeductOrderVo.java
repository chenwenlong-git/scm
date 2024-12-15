package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
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
public class DeductOrderVo {

    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "扣款状态")
    private DeductStatus deductStatus;

    @ApiModelProperty(value = "类型")
    private DeductType deductType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "扣款员工")
    private String deductUser;

    @ApiModelProperty(value = "扣款员工名称")
    private String deductUsername;

    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

}
