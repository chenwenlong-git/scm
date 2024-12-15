package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/11/6 17:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeductOrderImportationDto extends BaseImportationRowDto {

    @ApiModelProperty(value = "扣款类型")
    private String deductType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "约定结算时间")
    private String aboutSettleTime;

    @ApiModelProperty(value = "单据类型")
    private String deductOrderPurchaseType;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "数量")
    private String skuNum;

    @ApiModelProperty(value = "扣款原因")
    private String deductRemarks;

    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;


}
