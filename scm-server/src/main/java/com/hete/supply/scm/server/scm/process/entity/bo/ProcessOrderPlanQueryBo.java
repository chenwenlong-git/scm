package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年07月31日 15:53
 */
@Data
@NoArgsConstructor
public class ProcessOrderPlanQueryBo {
    @ApiModelProperty(value = "加工单号")
    private List<String> processOrderNosBySku;
    @ApiModelProperty(value = "通过原料sku获取加工单号")
    private List<String> processOrderNosByMaterialSku;
    @ApiModelProperty(value = "中间单据查询是否为空")
    private Boolean isEmpty;
}
