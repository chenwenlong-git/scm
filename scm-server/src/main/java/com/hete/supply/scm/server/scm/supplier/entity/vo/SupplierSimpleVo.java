package com.hete.supply.scm.server.scm.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/3 00:54
 */
@Data
@NoArgsConstructor
public class SupplierSimpleVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "产能")
    private Integer capacity;


    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;


}
