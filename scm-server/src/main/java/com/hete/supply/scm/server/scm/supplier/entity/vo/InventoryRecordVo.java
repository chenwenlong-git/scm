package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/1/8 20:39
 */
@Data
@NoArgsConstructor
public class InventoryRecordVo {
    @ApiModelProperty(value = "id")
    private Long supplierInventoryRecordId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "供应商仓库：备货，自备")
    private SupplierWarehouse supplierWarehouse;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "分类名(一级或二级名称)")
    private String categoryName;


    @ApiModelProperty(value = "供应商库存操作类型")
    private SupplierInventoryCtrlType supplierInventoryCtrlType;

    @ApiModelProperty(value = "操作前库存")
    private Integer beforeInventory;


    @ApiModelProperty(value = "操作数量")
    private Integer ctrlCnt;


    @ApiModelProperty(value = "操作后库存")
    private Integer afterInventory;

    @ApiModelProperty(value = "供应商库存操作原因")
    private SupplierInventoryCtrlReason supplierInventoryCtrlReason;


    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人")
    private String createUsername;

    @ApiModelProperty(value = "关联单据号")
    private String relateNo;

    @ApiModelProperty(value = "库存变更备注")
    private String recordRemark;

    @ApiModelProperty(value = "库存变更记录状态")
    private SupplierInventoryRecordStatus supplierInventoryRecordStatus;

    @ApiModelProperty(value = "审批人")
    private String approveUser;

    @ApiModelProperty(value = "审批人")
    private String approveUsername;

    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;
}
