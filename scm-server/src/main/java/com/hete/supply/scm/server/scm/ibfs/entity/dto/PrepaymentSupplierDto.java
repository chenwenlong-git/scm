package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/5/27 11:09
 */
@Data
@NoArgsConstructor
public class PrepaymentSupplierDto {
    @NotBlank(message = "供应商code不能为空")
    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;


    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;


    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @NotBlank(message = "对账单号不能为空")
    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;
}
