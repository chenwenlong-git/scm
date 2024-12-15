package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@ApiModel(description = "收货单创建质检单实体")
@Data
public class ReceiveOrderCreateQcBo {
    @ApiModelProperty(value = "收货单质检订单列表")
    private List<ReceiveOrderQcOrderBo> createQcOrderBos;
}
