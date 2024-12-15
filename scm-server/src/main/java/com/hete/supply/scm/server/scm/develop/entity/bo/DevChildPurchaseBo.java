package com.hete.supply.scm.server.scm.develop.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/9/5 21:38
 */
@Data
@NoArgsConstructor
public class DevChildPurchaseBo {
    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

}
