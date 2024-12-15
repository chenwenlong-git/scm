package com.hete.supply.scm.server.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/2/14 09:27
 */
@Data
@NoArgsConstructor
public class ShippingMarkNoVo {
    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;
}
