package com.hete.supply.scm.server.scm.cost.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/2/20 11:35
 */
@Data
@NoArgsConstructor
public class GoodsCostVo {

    @ApiModelProperty(value = "商品成本id")
    private Long costOfGoodsId;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "库存数量")
    private Integer inventory;

    @ApiModelProperty(value = "库存金额")
    private BigDecimal inventoryPrice;

    @ApiModelProperty(value = "加权单价")
    private BigDecimal weightingPrice;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库聚合维度")
    private PolymerizeWarehouse polymerizeWarehouse;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "成本时间")
    private String costTime;

    @ApiModelProperty(value = "成本时间类型")
    private CostTimeType costTimeType;
}
