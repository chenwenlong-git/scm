package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.DeductOrderPurchaseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/3/6 18:32
 */
@Data
@NoArgsConstructor
public class DeductSupplementBusinessBo {

    @ApiModelProperty(value = "单据类型")
    private DeductOrderPurchaseType deductOrderPurchaseType;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "SKU")
    private String sku;

}
