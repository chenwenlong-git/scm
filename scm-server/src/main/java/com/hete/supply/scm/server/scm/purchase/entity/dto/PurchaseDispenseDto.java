package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/4/11 09:47
 */
@Data
@NoArgsConstructor
public class PurchaseDispenseDto {
    @NotNull(message = "原料提供方不能为空")
    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;

    @ApiModelProperty(value = "出库单号")
    private List<String> deliveryOrderNoList;

    @ApiModelProperty(value = "id")
    private List<Long> supplierInventoryRecordIdList;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;
}
