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
public class GenerateSettleOrderSupplierVo {

    @ApiModelProperty(value = "供应商编码", example = "supplierCode")
    private String supplierCode;
}
