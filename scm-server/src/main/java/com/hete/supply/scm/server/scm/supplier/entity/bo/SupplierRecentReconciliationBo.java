package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/5/22 10:46
 */
@Data
@NoArgsConstructor
public class SupplierRecentReconciliationBo {

    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDateTime reconciliationStartTime;

    @ApiModelProperty(value = "对账周期结束时间")
    private LocalDateTime reconciliationEndTime;

}
