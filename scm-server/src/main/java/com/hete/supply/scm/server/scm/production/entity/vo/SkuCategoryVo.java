package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/24.
 */
@Data
public class SkuCategoryVo {
    @ApiModelProperty(value = "商品分类ID", notes = "商品二级分类ID")
    private Long skuCategoryId;

    @ApiModelProperty(value = "plm商品分类名称", notes = "商品二级分类名称")
    private String skuCategoryName;
}
