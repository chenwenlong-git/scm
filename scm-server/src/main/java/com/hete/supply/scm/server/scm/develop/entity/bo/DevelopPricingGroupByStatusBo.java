package com.hete.supply.scm.server.scm.develop.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/24 16:13
 */
@Data
@NoArgsConstructor
public class DevelopPricingGroupByStatusBo {

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "状态")
    private DevelopPricingOrderStatus developPricingOrderStatus;

}