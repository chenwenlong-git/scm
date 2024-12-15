package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
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
public class SupplementOrderVo {

    @ApiModelProperty(value = "补款ID")
    private Long supplementOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "补款状态")
    private SupplementStatus supplementStatus;

    @ApiModelProperty(value = "类型")
    private SupplementType supplementType;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "补款员工")
    private String supplementUser;

    @ApiModelProperty(value = "补款员工名称")
    private String supplementUsername;

    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;

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
