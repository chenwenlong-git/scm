package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(value = "质检明细信息", description = "质检明细信息")
public class ProcessOrderCreateQcOrderDetailBo {

    @ApiModelProperty(name = "容器编码")
    private String containerCode;

    @ApiModelProperty(name = "批次码")
    private String batchCode;

    @ApiModelProperty(name = "sku")
    private String skuCode;

    @ApiModelProperty(name = "应质检数量")
    private Integer amount;
}
