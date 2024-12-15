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
 * 生产信息产品规格书关联供应商
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_spec_supplier")
@ApiModel(value = "ProduceDataSpecSupplierPo对象", description = "生产信息产品规格书关联供应商")
public class ProduceDataSpecSupplierPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_spec_supplier_id", type = IdType.ASSIGN_ID)
    private Long produceDataSpecSupplierId;


    @ApiModelProperty(value = "生产信息产品规格书ID")
    private Long produceDataSpecId;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


}
