package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildIdAndVersionDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/17 16:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchasePassDto extends PurchaseChildIdAndVersionDto {
    @NotNull(message = "目标状态不能为空")
    @ApiModelProperty(value = "目标状态")
    private PurchaseOrderStatus targetStatus;

    @NotNull(message = "是否跳过状态不能为空")
    @ApiModelProperty(value = "是否跳过")
    private BooleanType isPass;
}
