package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/8/8 14:28
 */
@Data
@NoArgsConstructor
public class PurchaseChildNoListDto {

    @NotEmpty(message = "采购订单号不能为空")
    @ApiModelProperty(name = "采购订单号")
    private List<String> noList;
}
