package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderPo;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/8.
 */
@Data
public class InventoryMatchingValidationResultBo {
    private boolean isValid;
    private RepairOrderPo repairOrderPo;
    private List<ProcessOrderMaterialPo> processOrderMaterialPos;
}
