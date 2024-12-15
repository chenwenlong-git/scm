package com.hete.supply.scm.server.scm.cost.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/2/28 11:37
 */
@Data
@NoArgsConstructor
public class CostOfGoodsLogsPageDto extends ComPageDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long costOfGoodsId;

    @NotNull(message = "成本时间维度不能为空")
    @ApiModelProperty(value = "成本时间维度")
    private CostTimeType costTimeType;

}
