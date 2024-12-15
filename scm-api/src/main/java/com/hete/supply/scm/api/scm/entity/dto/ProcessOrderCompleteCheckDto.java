package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
public class ProcessOrderCompleteCheckDto {
    @ApiModelProperty(value = "质检单号")
    @NotBlank(message = "质检单号不能为空")
    private String checkOrderNo;

    @ApiModelProperty(value = "质检人")
    private String operator;

    @ApiModelProperty(value = "质检人")
    private String operatorName;

    @NotEmpty(message = "质检详情不能为空")
    private List<@Valid SkuItem> skus;

    @Data
    public static class SkuItem {

        @NotBlank(message = "sku 批次码不能为空")
        private String skuBatchCode;

        @NotNull(message = "正品数不能为空")
        @PositiveOrZero(message = "数量必须大于或等于0")
        private Integer qualityGoodsCnt;

        @NotNull(message = "次品数不能为空")
        @PositiveOrZero(message = "数量必须大于或等于0")
        private Integer defectiveGoodsCnt;

    }

}
