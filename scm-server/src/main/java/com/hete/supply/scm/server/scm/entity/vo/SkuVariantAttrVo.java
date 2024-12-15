package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmAttrSkuVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/10 16:35
 */
@Data
@NoArgsConstructor
public class SkuVariantAttrVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "变体属性")
    private List<PlmAttrSkuVo> variantSkuList;
}
