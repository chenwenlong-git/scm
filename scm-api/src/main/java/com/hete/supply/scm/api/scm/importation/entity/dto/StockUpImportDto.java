package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/1/29 10:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockUpImportDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "下单数")
    private Integer placeOrderCnt;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "要求回货时间")
    private String requestReturnGoodsDateStr;

    @ApiModelProperty(value = "要求回货时间")
    private LocalDateTime requestReturnGoodsDate;
}
