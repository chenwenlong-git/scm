package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/15 17:49
 */
@Data
@NoArgsConstructor
public class ShippingMarkNoListDto {
    @NotEmpty(message = "箱唛号不能为空")
    @ApiModelProperty(value = "箱唛号")
    private List<String> shippingMarkNoList;
}
