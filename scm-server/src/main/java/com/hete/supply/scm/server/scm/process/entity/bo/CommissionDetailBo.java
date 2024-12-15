package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.CommissionCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/15.
 */
@Data
public class CommissionDetailBo {

    @ApiModelProperty(value = "主键id")
    private Long scanCommissionDetailId;


    @ApiModelProperty(value = "扫码记录ID")
    private Long processOrderScanId;


    @ApiModelProperty(value = "提成类目")
    private CommissionCategory commissionCategory;


    @ApiModelProperty(value = "提成属性")
    private CommissionAttribute commissionAttribute;


    @ApiModelProperty(value = "数量")
    private Integer quantity;


    @ApiModelProperty(value = "单价参考金额")
    private BigDecimal unitCommission;

    @ApiModelProperty(value = "提成总金额")
    private BigDecimal totalAmount;
}
