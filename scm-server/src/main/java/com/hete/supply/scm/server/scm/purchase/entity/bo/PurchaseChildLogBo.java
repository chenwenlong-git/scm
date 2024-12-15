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
public class PurchaseChildLogBo {
    @ApiModelProperty(value = "更新人")
    final private String updateUsernameKey = "修改人";
    final private LogVersionValueType updateUsernameType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "更新时间")
    final private String updateTimeKey = "修改时间";
    final private LogVersionValueType updateTimeType = LogVersionValueType.DATE;
    @ApiModelProperty(value = "spu")
    final private String spuKey = "spu";
    final private LogVersionValueType spuType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "收货仓库名称")
    final private String warehouseNameKey = "收货仓库";
    final private LogVersionValueType warehouseNameType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "供应商名称")
    final private String supplierNameKey = "供应商";
    final private LogVersionValueType supplierNameType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "采购约定交期")
    final private String deliverDateKey = "采购约定交期";
    final private LogVersionValueType deliverDateType = LogVersionValueType.DATE;
    @ApiModelProperty(value = "平台")
    final private String platformKey = "平台名称";
    final private LogVersionValueType platformType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "采购单备注")
    final private String orderRemarksKey = "采购单备注";
    final private LogVersionValueType orderRemarksType = LogVersionValueType.STRING;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "采购约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;

}
