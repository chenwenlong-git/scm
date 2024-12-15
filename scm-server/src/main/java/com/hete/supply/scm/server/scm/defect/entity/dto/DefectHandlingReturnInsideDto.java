package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/6/26 16:27
 */
@Data
@NoArgsConstructor
public class DefectHandlingReturnInsideDto {

    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @NotEmpty(message = "次品记录不能为空")
    @ApiModelProperty(value = "次品记录")
    @Valid
    private List<DefectHandlingReturnInsideItemDto> defectHandlingReturnInsideItemList;

    @Data
    public static class DefectHandlingReturnInsideItemDto {
        @NotNull(message = "产品扣款单价不能为空")
        @ApiModelProperty(value = "产品扣款单价")
        private BigDecimal deductPrice;

        @NotEmpty(message = "次品处理单号不能为空")
        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;
    }
}
