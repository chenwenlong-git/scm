package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 生产信息详情关联供应商
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_item_supplier")
@ApiModel(value = "ProduceDataItemSupplierPo对象", description = "生产信息详情关联供应商")
public class ProduceDataItemSupplierPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_item_supplier_id", type = IdType.ASSIGN_ID)
    private Long produceDataItemSupplierId;


    @ApiModelProperty(value = "生产信息详情ID")
    private Long produceDataItemId;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


}
