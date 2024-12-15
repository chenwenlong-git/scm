package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/26.
 */
@Data
@ApiModel(description = "结算单卡片查询参数请求体DTO")
public class SettleOrderCardParamsVo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "结算单号列表")
    private List<String> settleOrderNos;
}
