package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
@Data
@ApiModel(description = "更新固定成本系数请求对象")
public class CostCoefficientUpdateRequestDto {

    private String key;

    @NotEmpty(message = "更新固定成本列表不能为空")
    private List<@Valid CostCoefficientUpdateInfoDto> costCoefficientUpdateInfoDtoList;

    @Data
    public static class CostCoefficientUpdateInfoDto {
        @ApiModelProperty(value = "生效时间", required = true, example = "2022-01-01")
        @NotNull(message = "生效时间不能为空")
        private LocalDateTime effectiveTime;

        @ApiModelProperty(value = "系数", example = "0.01")
        private BigDecimal coefficient;
    }
}
