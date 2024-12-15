package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class DefectHandlingReturnProcessDto {

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotEmpty(message = "次品记录不能为空")
    @ApiModelProperty(value = "次品记录")
    @Valid
    private List<DefectHandlingReturnInsideItemDto> defectHandlingReturnInsideItemList;

    @Data
    public static class DefectHandlingReturnInsideItemDto {

        @NotEmpty(message = "次品处理单号不能为空")
        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;
    }
}
