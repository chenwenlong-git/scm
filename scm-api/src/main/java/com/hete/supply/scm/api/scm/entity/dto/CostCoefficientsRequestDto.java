package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
@Data
@ApiModel(description = "成本系数请求对象")
public class CostCoefficientsRequestDto extends ComPageDto {
    @ApiModelProperty(value = "生效时间起始日期", example = "2022-01-01")
    private String effectiveTimeStart;

    @ApiModelProperty(value = "生效时间结束日期", example = "2022-12-31")
    private String effectiveTimeEnd;
}
