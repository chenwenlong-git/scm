package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/16 17:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseChildDetailDto extends PurchaseChildNoDto {
    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;

    @JsonIgnore
    @ApiModelProperty(value = "采购单状态")
    private List<PurchaseOrderStatus> purchaseOrderStatusList;

    @JsonIgnore
    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;
}
