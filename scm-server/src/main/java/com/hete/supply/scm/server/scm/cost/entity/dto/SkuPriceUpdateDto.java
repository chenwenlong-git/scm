package com.hete.supply.scm.server.scm.cost.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/10/23 14:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "修改sku价格mq入参")
public class SkuPriceUpdateDto extends BaseMqMessageDto {
    /**
     * 批次价格入参列表
     */
    @NotEmpty(message = "sku价格入参列表不能为空")
    @Size(max = 100, message = "sku价格入参列表不能超过100条")
    @Valid
    private List<SkuPriceDto> skuCodePriceList;


    @Data
    public static class SkuPriceDto {
        /**
         * 批次码
         */
        @NotBlank(message = "sku不能为空")
        private String skuCode;
        /**
         * 批次价格
         */
        @Digits(integer = 6, fraction = 2, message = "批次价格输入格式不正确")
        @DecimalMin(value = "0.00", message = "批次价格不能为负数")
        private BigDecimal price;
    }
}
