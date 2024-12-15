package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/5/29.
 */
@Data
@ApiModel(description = "结算单详情视图对象")
public class GenerateSettleOrderVo {

    @ApiModelProperty(value = "对账单号", example = "DZ")
    private String recoOrderNo;
}
