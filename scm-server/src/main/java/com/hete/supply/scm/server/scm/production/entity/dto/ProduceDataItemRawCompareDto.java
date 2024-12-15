package com.hete.supply.scm.server.scm.production.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 生产资料原料对照关系
 *
 * @author yanjiawei
 * Created on 2024/11/11.
 */
@Data
public class ProduceDataItemRawCompareDto {
    @NotBlank(message = "对照商品sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @JsonProperty("skuCnt")
    @Positive(message = "对照商品数量必须大于0")
    @Digits(integer = 6, fraction = 2, message = "对照商品数量最多6位，小数最多2位")
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
}
