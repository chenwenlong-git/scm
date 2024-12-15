package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:52
 */
@Data
@NoArgsConstructor
public class FinanceRecoFundTypeDto {

    @ApiModelProperty(value = "款项类型")
    @NotNull(message = "款项类型不能为空")
    private FinanceRecoFundType financeRecoFundType;

}
