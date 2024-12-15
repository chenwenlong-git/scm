package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(value = "创建质检结果实体", description = "创建质检结果实体")
public class ReceiveOrderCreateQcResultBo {
    @ApiModelProperty(value = "质检单创建结果列表")
    private List<ReceiveOrderCreateQcOrderResultBo> qcOrderResultBos;
}
