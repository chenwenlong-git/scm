package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.po.CostCoefficientsPo;
import com.hete.supply.scm.server.scm.process.entity.vo.CostCoefficientsVo;

import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
public class CostCoefficientsConverter {

    public static CostCoefficientsVo convertToVo(CostCoefficientsPo po) {
        CostCoefficientsVo vo = new CostCoefficientsVo();
        if (Objects.isNull(po)) {
            return vo;
        }

        vo.setCostCoefficientsId(po.getCostCoefficientsId());
        vo.setEffectiveTime(po.getEffectiveTime());
        vo.setCoefficient(po.getCoefficient());
        return vo;
    }
}
