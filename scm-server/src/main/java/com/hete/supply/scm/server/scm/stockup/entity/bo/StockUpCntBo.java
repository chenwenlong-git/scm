package com.hete.supply.scm.server.scm.stockup.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/22 21:06
 */
@Data
@NoArgsConstructor
public class StockUpCntBo {
    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;

    @ApiModelProperty(value = "回货数")
    private Integer returnGoodsCnt;
}
