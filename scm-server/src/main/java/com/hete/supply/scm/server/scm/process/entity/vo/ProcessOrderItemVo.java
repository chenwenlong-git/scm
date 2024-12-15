package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderItemVo {

    @ApiModelProperty(value = "加工产品明细 id")
    private Long processOrderItemId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "变体名称")
    private String variantProperties;

    @ApiModelProperty(value = "加工数量")
    private Integer processNum;

    @ApiModelProperty(value = "采购单价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "是否是首次创建，是：'true'，次品赋码设置为 \"false\"")
    private BooleanType isFirst;

    @ApiModelProperty("版本号")
    private Integer version;
}
