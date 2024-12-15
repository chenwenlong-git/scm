package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 时间范围查询供应商产能实体
 *
 * @author yanjiawei
 * Created on 2024/8/8.
 */
@Data
public class SupCapDateRangeQueryBo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "开始时间")
    private LocalDate beginCapacityDate;

    @ApiModelProperty(value = "结束时间")
    private LocalDate endCapacityDate;
}
