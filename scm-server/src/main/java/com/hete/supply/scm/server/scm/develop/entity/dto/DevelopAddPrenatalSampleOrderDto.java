package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/10/20 18:26
 */
@Data
@NoArgsConstructor
public class DevelopAddPrenatalSampleOrderDto {

    @ApiModelProperty("开发子单列表信息")
    @Valid
    @NotEmpty(message = "开发子单列表信息不能为空")
    private List<DevelopAddPrenatalSampleOrderItem> developAddPrenatalSampleOrderItemList;

    @Data
    public static class DevelopAddPrenatalSampleOrderItem {

        @NotBlank(message = "开发子单号不能为空")
        @ApiModelProperty(value = "开发子单号")
        private String developChildOrderNo;

        @ApiModelProperty(value = "加工数量")
        @NotNull(message = "加工数不能为空")
        @Positive(message = "加工数必须为正整数")
        @Min(value = 1, message = "加工数不能小于1")
        @Max(value = 100, message = "加工数不能大于100")
        private Integer processNum;

    }


}
