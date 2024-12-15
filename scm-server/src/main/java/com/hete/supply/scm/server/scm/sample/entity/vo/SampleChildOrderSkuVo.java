package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmAttrSkuVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/4/14 14:17
 */
@Data
@NoArgsConstructor
public class SampleChildOrderSkuVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "变体")
    private List<PlmAttrSkuVo> variantSkuList;

}
