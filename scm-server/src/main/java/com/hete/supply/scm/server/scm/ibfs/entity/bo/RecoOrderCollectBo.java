package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/5/30 14:14
 */
@Data
@NoArgsConstructor
public class RecoOrderCollectBo {

    @ApiModelProperty(value = "对账应收金额")
    private BigDecimal receivePrice;


    @ApiModelProperty(value = "对账应付金额")
    private BigDecimal payPrice;

}
