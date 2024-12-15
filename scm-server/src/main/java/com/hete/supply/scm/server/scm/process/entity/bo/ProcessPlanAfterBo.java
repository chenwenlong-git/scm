package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年08月03日 01:07
 */
@Data
@AllArgsConstructor
public class ProcessPlanAfterBo {
    private List<EmployeeProcessAbilityBo> productionCapacityPool;
    private List<ProcessProcedureEmployeePlanBo> newProductionSchedule;
    private ProcessOrderPo processOrderPo;
}
