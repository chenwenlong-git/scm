package com.hete.supply.scm.server.scm.cost;

import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.supply.scm.server.scm.process.entity.bo.MonthStartWeightAvgPriceResultBo;

/**
 * @author yanjiawei
 * Created on 2024/3/21.
 */
public class CostBuilder {

    public static CostImportDto buildCostImportDto(MonthStartWeightAvgPriceResultBo monthStartWeightAvgPriceResultBo) {
        CostImportDto costImportDto = new CostImportDto();
        costImportDto.setSku(monthStartWeightAvgPriceResultBo.getSku());
        costImportDto.setMoWeightingPriceMin(monthStartWeightAvgPriceResultBo.getMonthStartWeightedAveragePrice());
        return costImportDto;
    }
}
