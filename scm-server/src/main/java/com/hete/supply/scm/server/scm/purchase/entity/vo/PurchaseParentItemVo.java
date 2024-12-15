package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/1/16 14:42
 */
@Data
@NoArgsConstructor
public class PurchaseParentItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;
}
