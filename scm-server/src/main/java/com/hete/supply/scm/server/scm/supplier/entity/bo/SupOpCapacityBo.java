package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author yanjiawei
 * Created on 2024/8/6.
 */
@Data
public class SupOpCapacityBo {
    @ApiModelProperty(value = "供应商编码", notes = "必填")
    private String supplierCode;

    @ApiModelProperty(value = "操作日期", notes = "必填字段：需要增加/扣减某个日期的产能")
    private LocalDate operateDate;

    @ApiModelProperty(value = "操作值", notes = "必填字段 增加：+n 扣减：-n", example = "+1 / -1")
    private BigDecimal operateValue;

    @ApiModelProperty(value = "业务单号", notes = "非必填字段，标识操作产能相关业务单据")
    private String bizNo;
}
