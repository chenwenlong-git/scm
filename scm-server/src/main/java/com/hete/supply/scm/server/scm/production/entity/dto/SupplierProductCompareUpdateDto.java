package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/10/14 10:58
 */
@Data
@NoArgsConstructor
public class SupplierProductCompareUpdateDto {

    @NotNull(message = "plm的产品ID不能为空")
    @ApiModelProperty(value = "plm的产品ID")
    private Long plmSkuId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "生产周期")
    @DecimalMin(value = "0", message = "生产周期是数字")
    private BigDecimal cycle;

    @ApiModelProperty(value = "单件产能")
    @NotNull(message = "单件产能不能为空")
    @DecimalMin(value = "0", message = "单件产能是非负数")
    @Digits(integer = 8, fraction = 2, message = "单件产能小数位数不能超过两位")
    private BigDecimal singleCapacity;

    @ApiModelProperty(value = "供应商产品对照列表")
    @Valid
    private List<SupplierProductCompareItemDto> supplierProductCompareList;


}
