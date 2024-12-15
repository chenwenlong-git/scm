package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * @date 2023年08月09日 10:33
 */
@Data
@ApiModel(value = "通过加工单号查询扫码详情参数", description = "通过加工单号查询扫码详情参数")
public class H5ProcessOrderProcedureScanDetailDto {
    @ApiModelProperty(value = "用户Userkey")
    @NotBlank(message = "加工单编号不能为空")
    private String processOrderNo;
}
