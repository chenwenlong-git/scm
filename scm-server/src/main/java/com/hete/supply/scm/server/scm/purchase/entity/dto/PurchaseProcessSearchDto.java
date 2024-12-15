package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseProductSearchDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseProcessSearchDto extends PurchaseProductSearchDto {
    @ApiModelProperty(value = "wms收货时间")
    private LocalDateTime wmsReceiptTimeStart;

    @ApiModelProperty(value = "wms收货时间")
    private LocalDateTime wmsReceiptTimeEnd;

    @ApiModelProperty(value = "wms质检时间")
    private LocalDateTime wmsQcTimeStart;

    @ApiModelProperty(value = "wms质检时间")
    private LocalDateTime wmsQcTimeEnd;


    @ApiModelProperty(value = "wms入库时间")
    private LocalDateTime wmsWarehousingTimeStart;


    @ApiModelProperty(value = "wms入库时间")
    private LocalDateTime wmsWarehousingTimeEnd;

    @ApiModelProperty(value = "wms退货时间")
    private LocalDateTime wmsReturnTimeStart;

    @ApiModelProperty(value = "wms退货时间")
    private LocalDateTime wmsReturnTimeEnd;
}
