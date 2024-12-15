package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderItemPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Data
@ApiModel(description = "创建财务结算订单业务对象")
public class CreateFinanceSettleOrderBo {

    @ApiModelProperty(value = "供应商编码", example = "S001")
    private String supplierCode;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

    @ApiModelProperty(value = "跟单采购员")
    private String followUser;

    @ApiModelProperty(value = "结算明细列表")
    private List<FinanceSettleOrderItemPo> settleOrderItems;
}
