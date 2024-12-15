package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/22 16:36
 */
@Data
@NoArgsConstructor
public class PurchaseChildCapacityLogBo {
    @ApiModelProperty(value = "变更节点")
    final private String purchaseStatusKey = "变更节点";
    final private LogVersionValueType purchaseStatusType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "供应商")
    final private String supplierCodeKey = "供应商";
    final private LogVersionValueType supplierCodeType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "日期")
    final private String updateTimeKey = "日期";
    final private LogVersionValueType updateTimeType = LogVersionValueType.DATE;
    @ApiModelProperty(value = "变更后所需产能")
    final private String updateCapacityKey = "变更后所需产能";
    final private LogVersionValueType updateCapacityType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "操作人")
    final private String updateUsernameKey = "操作人";
    final private LogVersionValueType updateUsernameType = LogVersionValueType.STRING;

    @ApiModelProperty(value = "变更节点")
    private String purchaseStatus;

    @ApiModelProperty(value = "供应商")
    private String supplierCode;

    @ApiModelProperty(value = "日期")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "变更后所需产能")
    private String updateCapacity;

    @ApiModelProperty(value = "操作人")
    private String updateUsername;
}
