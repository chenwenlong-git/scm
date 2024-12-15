package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 供应商库存记录表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_inventory_record")
@ApiModel(value = "SupplierInventoryRecordPo对象", description = "供应商库存记录表")
public class SupplierInventoryRecordPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_inventory_record_id", type = IdType.ASSIGN_ID)
    private Long supplierInventoryRecordId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "供应商仓库：备货，自备")
    private SupplierWarehouse supplierWarehouse;

    @ApiModelProperty(value = "SPU")
    private String spu;


    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "商品类目ID")
    private Long categoryId;

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
