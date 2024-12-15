package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 18:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ConfirmCommissioningDto extends PurchaseChildNoDto {

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "投产原料消耗列表")
    @Valid
    private List<ConfirmCommissioningItemDto> confirmCommissioningItemList;

    @ApiModelProperty(value = "投产数量")
    private Integer commissionCnt;
}
