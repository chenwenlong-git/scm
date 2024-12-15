package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/4/10 09:47
 */
@Data
@NoArgsConstructor
public class PurchaseProductSplitItemDto {
    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotNull(message = "业务约定交期不能为空")
    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "采购产品明细列表")
    @Valid
    @NotEmpty(message = "采购产品明细列表不能为空")
    private List<PurchaseProductItemDto> purchaseProductItemList;
}
