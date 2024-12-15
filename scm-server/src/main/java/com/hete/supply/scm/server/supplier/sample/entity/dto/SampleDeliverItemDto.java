package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/11/14 17:29
 */
@Data
@NoArgsConstructor
public class SampleDeliverItemDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "spu")
    @NotBlank(message = "spu不能为空")
    @Length(max = 32, message = "spu长度不能超过32个字符")
    private String spu;

    @ApiModelProperty(value = "打样单价")
    @NotNull(message = "打样单价不能为空")
    private BigDecimal proofingPrice;

    @ApiModelProperty(value = "发货数")
    @NotNull(message = "发货数不能为空")
    @Min(value = 1, message = "发货数不能小于0")
    private Integer deliverCnt;

    @ApiModelProperty(value = "采购数")
    @NotNull(message = "采购数不能为空")
    @Min(value = 1, message = "采购数不能小于0")
    private Integer purchaseCnt;

}
