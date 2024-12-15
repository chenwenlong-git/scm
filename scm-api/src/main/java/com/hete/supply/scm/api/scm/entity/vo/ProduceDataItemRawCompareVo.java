package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 生产资料原料对照关系列表
 *
 * @author yanjiawei
 * Created on 2024/11/11.
 */
@Data
public class ProduceDataItemRawCompareVo {
    @ApiModelProperty(value = "主键id")
    private Long skuCompareId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @JsonProperty("skuCnt")
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
}
