package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.entity.bo.ProduceDataUpdatePurchasePriceBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/3/26 14:09
 */
@Data
@NoArgsConstructor
public class ProduceDataPurchasePriceDto {

    @ApiModelProperty(value = "SKU信息列表")
    private List<ProduceDataUpdatePurchasePriceBo> produceDataUpdatePurchasePriceBoList;

}
