package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.api.scm.entity.vo.MaterialBackVo;
import com.hete.supply.scm.server.scm.entity.bo.MaterialBackBo;

import java.util.Objects;

/**
 * 归还原料转化实体
 *
 * @author yanjiawei
 * @date 2023年07月03日 14:02
 */
public class ProcessMaterialBackConverter {

    public static MaterialBackVo convertToVo(String skuBatchCode, MaterialBackBo.MaterialBackSku materialBackBo) {
        MaterialBackVo materialBackVo = new MaterialBackVo();
        materialBackVo.setSkuBatchCode(skuBatchCode);

        if (Objects.isNull(materialBackBo)) {
            materialBackVo.setAvailableBackNum(0);
        } else {
            materialBackVo.setAvailableBackNum(materialBackBo.getAvailableBackNum());
        }
        return materialBackVo;
    }
}
