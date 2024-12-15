package com.hete.supply.scm.server.scm.stockup.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 20:10
 */
@Data
@NoArgsConstructor
public class StockUpCreateDto {
    @NotEmpty(message = "备货明细不能为空")
    @Valid
    @ApiModelProperty(value = "备货明细")
    private List<StockUpCreateItemDto> stockUpCreateItemList;
}
