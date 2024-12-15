package com.hete.supply.scm.server.scm.cost.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 商品成本表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cost_of_goods")
@ApiModel(value = "CostOfGoodsPo对象", description = "商品成本表")
public class CostOfGoodsPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "cost_of_goods_id", type = IdType.ASSIGN_ID)
    private Long costOfGoodsId;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;


    @ApiModelProperty(value = "仓库类型")
    private WmsEnum.WarehouseType warehouseTypes;


    @ApiModelProperty(value = "聚合类型：单仓、多仓")
    private PolymerizeType polymerizeType;


    @ApiModelProperty(value = "聚合仓库维度")
    private PolymerizeWarehouse polymerizeWarehouse;


    @ApiModelProperty(value = "成本时间类型:月、日")
    private CostTimeType costTimeType;


    @ApiModelProperty(value = "成本时间")
    private String costTime;


    @ApiModelProperty(value = "库存数量")
    private Integer inventory;


    @ApiModelProperty(value = "库存价格")
    private BigDecimal inventoryPrice;


    @ApiModelProperty(value = "加权单价")
    private BigDecimal weightingPrice;


}
