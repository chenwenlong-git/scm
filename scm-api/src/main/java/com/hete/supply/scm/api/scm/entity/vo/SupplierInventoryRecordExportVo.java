package com.hete.supply.scm.api.scm.entity.vo;

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
 * @date 2024/1/22 20:38
 */
@Data
@NoArgsConstructor
public class SupplierInventoryRecordExportVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "商品二级类目")
    private String categoryName;

    @ApiModelProperty(value = "供应商仓库：备货，自备")
    private SupplierWarehouse supplierWarehouse;

    @ApiModelProperty(value = "供应商仓库：备货，自备")
    private String supplierWarehouseStr;

    @ApiModelProperty(value = "供应商库存操作类型")
    private SupplierInventoryCtrlType supplierInventoryCtrlType;

    @ApiModelProperty(value = "供应商库存操作类型")
    private String supplierInventoryCtrlTypeStr;

    @ApiModelProperty(value = "操作前库存")
    private Integer beforeInventory;

    @ApiModelProperty(value = "操作数量")
    private Integer ctrlCnt;

    @ApiModelProperty(value = "操作后库存")
    private Integer afterInventory;

    @ApiModelProperty(value = "供应商库存操作原因")
    private SupplierInventoryCtrlReason supplierInventoryCtrlReason;

    @ApiModelProperty(value = "供应商库存操作原因")
    private String supplierInventoryCtrlReasonStr;

    @ApiModelProperty(value = "关联单据号")
    private String relateNo;

    @ApiModelProperty(value = "（单据）sku")
    private String relateNoSku;

    @ApiModelProperty(value = "（单据）产品名称")
    private String relateSkuEncode;

    @ApiModelProperty(value = "供应商产品名称")
    private String relateSkuSupplierProductName;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作时间")
    private String createTimeStr;

    @ApiModelProperty(value = "操作人")
    private String createUsername;

    @ApiModelProperty(value = "库存变更记录状态")
    private SupplierInventoryRecordStatus supplierInventoryRecordStatus;

    @ApiModelProperty(value = "库存变更记录状态字符串")
    private String supplierInventoryRecordStatusStr;
}
