package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 10:42
 */
@Data
public class ProduceDataAttrDetailVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商品属性")
    private List<ProduceDataAttrItemVo> produceDataAttrList;

}
