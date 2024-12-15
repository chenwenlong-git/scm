package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/12 17:22
 */
@Data
@NoArgsConstructor
public class DevelopChildSubmitOrderDto {
    @NotEmpty(message = "下单列表不能为空")
    @ApiModelProperty(value = "下单列表")
    @Valid
    private List<DevelopChildSubmitOrderItemDto> developChildSubmitOrderItemList;


}
