package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.scm.server.scm.ibfs.enums.SupplierConfirmResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@ApiModel(description = "跟单确认请求数据传输对象")
public class SupplierConfirmOrderDto {

    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号", required = true, example = "SETTLE123456")
    private String settleOrderNo;

    @ApiModelProperty(value = "版本号", required = true, example = "1.0")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @NotNull(message = "跟单确认结果不能为空")
    @ApiModelProperty(value = "跟单确认结果", example = "1.0")
    private SupplierConfirmResult supplierConfirmResult;

    @ApiModelProperty(value = "备注")
    @Length(max = 500, message = "备注字符长度不能超过 500 位")
    private String remarks;
}
