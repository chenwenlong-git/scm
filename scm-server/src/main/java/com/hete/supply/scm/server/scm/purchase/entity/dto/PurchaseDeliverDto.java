package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 01:05
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverDto {
    @NotBlank(message = "箱唛号不能为空")
    @ApiModelProperty(value = "箱唛号")
    @Length(max = 32, message = "箱唛号不能超过32位")
    private String shippingMarkNo;

    @ApiModelProperty(value = "运单号")
    @Length(max = 32, message = "运单号不能超过32位")
    private String trackingNo;

    @ApiModelProperty(value = "发货明细列表")
    @NotEmpty(message = "发货明细列表不能为空")
    @Valid
    private List<PurchaseDeliverItemDto> purchaseDeliverItemList;
}
