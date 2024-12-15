package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.bo.RepairQcDetailCreateBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(value = "创建质检实体", description = "创建质检实体")
public class RepairOrderCreateQcBo {
    private String repairOrderNo;
    private String warehouseCode;
    private List<RepairQcDetailCreateBo> repairQcDetailCreateBos;

    @ApiModelProperty(value = "平台")
    private String platform;
}
