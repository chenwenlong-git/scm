package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author yanjiawei
 * @Description TODO
 * @Date 2024/8/10 14:19
 */
@Data
@NoArgsConstructor
public class SupplierRestExportVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "停工日期")
    private LocalDate restDate;

    @ApiModelProperty(value = "停工日期")
    private String restDateStr;
}
