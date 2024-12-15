package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/24 10:15
 */
@Data
@NoArgsConstructor
public class RecoOrderExamineDto extends RecoOrderNoAndVersionDto {

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "审核状态")
    private BooleanType examine;

}
