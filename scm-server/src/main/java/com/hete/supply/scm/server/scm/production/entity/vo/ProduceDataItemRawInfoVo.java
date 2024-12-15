package com.hete.supply.scm.server.scm.production.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataItemRawCompareVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/27 14:24
 */
@Data
@NoArgsConstructor
public class ProduceDataItemRawInfoVo {
    @ApiModelProperty(value = "主键id")
    private Long produceDataItemRawId;

    @ApiModelProperty(value = "类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "商品对照关系")
    @JsonProperty("prodRawCompareList")
    private List<ProduceDataItemRawCompareVo> prodRawCompareVoList;
}
