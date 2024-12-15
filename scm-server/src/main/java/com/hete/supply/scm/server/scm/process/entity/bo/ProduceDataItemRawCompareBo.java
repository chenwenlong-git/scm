package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 生产资料原料对照关系
 *
 * @author yanjiawei
 * Created on 2024/11/9.
 */
@Data
public class ProduceDataItemRawCompareBo {
    @ApiModelProperty(value = "主键id")
    private Long produceDataItemRawCompareId;

    @ApiModelProperty(value = "生产信息详情原料表id")
    private Long produceDataItemRawId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "扣款金额")
    private BigDecimal quantity;
}
