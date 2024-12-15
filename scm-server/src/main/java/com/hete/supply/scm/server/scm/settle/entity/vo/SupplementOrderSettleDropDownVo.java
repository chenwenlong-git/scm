package com.hete.supply.scm.server.scm.settle.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/1/31 10:24
 */
@Data
@NoArgsConstructor
public class SupplementOrderSettleDropDownVo {

    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

}
