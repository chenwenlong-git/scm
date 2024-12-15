package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/7 09:31
 */
@Data
@NoArgsConstructor
public class GoodsProcessNewExportVo {
    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "工序")
    private String processSecondName;

}
