package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:07
 */
@Data
public class SupplementOrderExportVo {


    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "类型")
    private SupplementType supplementType;

    @ApiModelProperty(value = "补款状态")
    private SupplementStatus supplementStatus;

    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "补款员工")
    private String supplementUser;

    @ApiModelProperty(value = "补款员工名称")
    private String supplementUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;


}
