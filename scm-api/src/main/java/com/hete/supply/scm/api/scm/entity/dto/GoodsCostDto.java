package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/20 11:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GoodsCostDto extends ComPageDto {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "昨日库存数量")
    private Integer yestInventoryMin;

    @ApiModelProperty(value = "昨日库存数量")
    private Integer yestInventoryMax;

    @ApiModelProperty(value = "昨日库存金额")
    private BigDecimal yestInventoryPriceMin;

    @ApiModelProperty(value = "昨日库存金额")
    private BigDecimal yestInventoryPriceMax;

    @ApiModelProperty(value = "昨日加权单价")
    private BigDecimal yestWeightingPriceMin;

    @ApiModelProperty(value = "昨日加权单价")
    private BigDecimal yestWeightingPriceMax;


    @ApiModelProperty(value = "月初库存数量")
    private Integer moInventoryMin;

    @ApiModelProperty(value = "月初库存数量")
    private Integer moInventoryMax;

    @ApiModelProperty(value = "月初库存金额")
    private BigDecimal moInventoryPriceMin;

    @ApiModelProperty(value = "月初库存金额")
    private BigDecimal moInventoryPriceMax;

    @ApiModelProperty(value = "月初加权单价")
    private BigDecimal moWeightingPriceMin;

    @ApiModelProperty(value = "月初加权单价")
    private BigDecimal moWeightingPriceMax;

    @NotNull(message = "仓库维度不能为空")
    @ApiModelProperty(value = "聚合仓库维度")
    private PolymerizeType polymerizeType;

    @ApiModelProperty(value = "成本时间")
    private String costTime;

    @ApiModelProperty(value = "仓库名称")
    private List<String> warehouseNameList;

    @ApiModelProperty(value = "聚合仓库维度")
    private PolymerizeWarehouse polymerizeWarehouse;

    @NotNull(message = "成本时间维度不能为空")
    @ApiModelProperty(value = "成本时间维度")
    private CostTimeType costTimeType;

    @ApiModelProperty(value = "分页是否进行count查询(默认true)")
    private Boolean searchCount;

    @ApiModelProperty(value = "是否导出操作")
    private Boolean isExport;

}
