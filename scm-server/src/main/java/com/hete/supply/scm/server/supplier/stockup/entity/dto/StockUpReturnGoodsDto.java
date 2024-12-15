package com.hete.supply.scm.server.supplier.stockup.entity.dto;

import com.hete.supply.scm.server.scm.stockup.entity.dto.StockIdAndVersionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/10 14:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockUpReturnGoodsDto extends StockIdAndVersionDto {

    @NotNull(message = "入库数不能为空")
    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;

    @NotNull(message = "回货数不能为空")
    @ApiModelProperty(value = "回货数")
    private Integer returnGoodsCnt;
}
