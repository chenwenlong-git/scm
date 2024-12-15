package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialComparePo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 加工单原料关联业务对象
 *
 * @author yanjiawei
 * Created on 2024/11/10.
 */
@Data
public class ProcessOrderMaterialRefBo {

    @ApiModelProperty(value = "加工单原料")
    private ProcessOrderMaterialPo procMaterialPo;

    @ApiModelProperty(value = "原料对照关系列表")
    private List<ProcessOrderMaterialComparePo> procMaterialComparePoList;
}
