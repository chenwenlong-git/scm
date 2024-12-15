package com.hete.supply.scm.server.scm.stockup.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/1/9 20:04
 */
@Data
@NoArgsConstructor
public class StockUpSearchItemVo {

    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "回货时间")
    private LocalDateTime returnGoodsDate;

    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;


    @ApiModelProperty(value = "回货数")
    private Integer returnGoodsCnt;
}
