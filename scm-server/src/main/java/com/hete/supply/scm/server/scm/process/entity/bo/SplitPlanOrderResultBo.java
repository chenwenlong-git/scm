package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderPo;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
@Data
public class SplitPlanOrderResultBo {
    private RepairOrderPo repairOrderPo;
    private List<RepairOrderItemPo> repairOrderItemPos;
    private List<ProcessOrderMaterialPo> processOrderMaterialPos;
}
