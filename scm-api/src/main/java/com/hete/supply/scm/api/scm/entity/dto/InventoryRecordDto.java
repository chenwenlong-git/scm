package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/8 20:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class InventoryRecordDto extends ComPageDto {

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "供应商库存操作原因")
    private SupplierInventoryCtrlReason supplierInventoryCtrlReason;

    @ApiModelProperty(value = "供应商库存操作原因列表")
    private List<SupplierInventoryCtrlReason> supplierInventoryCtrlReasonList;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商仓库：备货，自备")
    private List<SupplierWarehouse> supplierWarehouseList;

    @ApiModelProperty(value = "供应商库存操作类型")
    private SupplierInventoryCtrlType supplierInventoryCtrlType;

    @ApiModelProperty(value = "供应商库存操作类型列表")
    private List<SupplierInventoryCtrlType> supplierInventoryCtrlTypeList;

    @ApiModelProperty(value = "操作时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "操作时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "操作人")
    private String createUser;

    @ApiModelProperty(value = "操作人")
    private String createUsername;


    @ApiModelProperty(value = "商品类目id")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "skuList(用于产品名称查询)")
    private List<String> skuList;

    @Size(max = 200, message = "导出勾选最多一次性勾选200条")
    @ApiModelProperty(value = "idList(用于导出)")
    private List<Long> idList;

    @ApiModelProperty(value = "关联单据号")
    private String relateNo;

    @ApiModelProperty(value = "库存变更记录状态")
    private List<SupplierInventoryRecordStatus> supplierInventoryRecordStatusList;
}
