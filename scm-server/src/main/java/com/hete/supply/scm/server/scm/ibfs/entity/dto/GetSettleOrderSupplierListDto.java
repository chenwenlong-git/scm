package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/5/28.
 */
@Data
@ApiModel(description = "获取结算单供应商信息请求数据传输对象")
public class GetSettleOrderSupplierListDto {
    @ApiModelProperty(value = "供应商编码", example = "SUP123")
    private String supplierCode;

    @ApiModelProperty(value = "结算单号", example = "SETTLE123456")
    private String settleOrderNo;

    @ApiModelProperty(value = "对账单号", example = "RECO123456")
    private String recoOrderNo;
}
