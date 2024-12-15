package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/6/26 16:27
 */
@Data
@NoArgsConstructor
public class DefectHandlingExchangeGoodsDto {
    @ApiModelProperty(value = "仓库编码")
    @NotBlank(message = "仓库编码不能为空")
    @Length(max = 32, message = "仓库编号字符长度不能超过 32 位")
    private String warehouseCode;

    @NotEmpty(message = "次品记录不能为空")
    @ApiModelProperty(value = "次品记录")
    @Valid
    private List<DefectHandlingExchangeSkuDto> defectHandlingExchangeSkuList;

    @Data
    public static class DefectHandlingExchangeSkuDto {
        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotEmpty(message = "次品处理单号不能为空")
        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;
    }
}
