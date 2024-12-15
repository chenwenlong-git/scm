package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/5/31.
 */
@Data
@ApiModel(description = "创建结算单请求数据传输对象")
@NoArgsConstructor
public class GenerateSettleOrderDto {

    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty(value = "供应商编码", required = true, example = "SETTLE123456")
    private String supplierCode;
}
