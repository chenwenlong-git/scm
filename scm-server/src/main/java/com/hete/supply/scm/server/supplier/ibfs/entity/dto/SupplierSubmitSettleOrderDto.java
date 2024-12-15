package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@ApiModel(description = "提交结算单请求数据传输对象")
@NoArgsConstructor
public class SupplierSubmitSettleOrderDto {

    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号", required = true, example = "SETTLE123456")
    private String settleOrderNo;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号", required = true, example = "1.0")
    private Integer version;
}
