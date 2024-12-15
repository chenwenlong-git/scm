package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/8.
 */
@Data
public class RepairAllocationResultBo {
    @ApiModelProperty(value = "返修单序号")
    private int no;

    @ApiModelProperty(value = "返修sku列表")
    private List<RepairOrderItemBo> repairOrderItems;

    @ApiModelProperty(value = "返修原料列表")
    private List<RepairMaterialBo> repairMaterials;
}
