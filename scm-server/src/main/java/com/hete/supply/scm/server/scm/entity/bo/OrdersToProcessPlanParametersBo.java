package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.OverPlan;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * 需要排产加工单信息
 *
 * @author yanjiawei
 * @date 2023/07/27 23:43
 */
@Data
@NoArgsConstructor
public class OrdersToProcessPlanParametersBo {

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;
    @ApiModelProperty(value = "加工单类型")
    private Set<ProcessOrderType> processOrderTypes;
    @ApiModelProperty(value = "是否超额")
    private OverPlan overPlan;
    @ApiModelProperty("工序状态,ENABLED:启用，DISABLED：禁用")
    private ProcessStatus processStatus;
}