package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseChildEditImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "期望上架时间")
    private String deliverDateStr;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "答交时间")
    private String promiseDateStr;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;
}
