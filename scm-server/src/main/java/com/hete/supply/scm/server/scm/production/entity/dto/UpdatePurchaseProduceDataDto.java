package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 16:06
 */
@Data
public class UpdatePurchaseProduceDataDto {

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "plm_sku的版本号")
    private Integer version;

    @ApiModelProperty(value = "生产周期")
    @DecimalMin(value = "0", message = "生产周期是数字")
    private BigDecimal cycle;

    @ApiModelProperty(value = "单件产能")
    @NotNull(message = "单件产能不能为空")
    @DecimalMin(value = "0", message = "单件产能是非负数")
    @Digits(integer = 8, fraction = 2, message = "单件产能小数位数不能超过两位")
    private BigDecimal singleCapacity;

    @ApiModelProperty(value = "商品采购价格")
    @NotNull(message = "商品采购价格不能为空")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "供应商产品对照列表")
    private List<@Valid SupplierProductCompareItemDto> supplierProductCompareItemList;

}
