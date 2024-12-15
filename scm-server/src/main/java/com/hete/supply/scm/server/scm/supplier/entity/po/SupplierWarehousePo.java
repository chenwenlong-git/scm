package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商仓库表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_warehouse")
@ApiModel(value = "SupplierWarehousePo对象", description = "供应商仓库表")
public class SupplierWarehousePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键ID")
    @TableId(value = "supplier_warehouse_id", type = IdType.ASSIGN_ID)
    private Long supplierWarehouseId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;


    @ApiModelProperty(value = "仓库类型")
    private SupplierWarehouse supplierWarehouse;


}
