package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 原料信息(数量信息)
 *
 * @author yanjiawei
 * @date 2023/07/23 17:57
 */
@Data
@NoArgsConstructor
@ApiModel(value = "原料信息(数量信息)", description = "原料信息(数量信息)")
public class MaterialNumInformationVo {

    @ApiModelProperty(value = "sku")
    private String sku;
    @ApiModelProperty(value = "下单数")
    private Integer orderNum;
    @ApiModelProperty(value = "出库数")
    private Integer deliveryNum;
    @ApiModelProperty(value = "收货数")
    private Integer receiptNum;
}