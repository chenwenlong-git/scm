package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @author yanjiawei
 * Created on 2024/3/13.
 */

@Data
@ApiModel(description = "加工出货人效报表")
public class ProcLaborEfficiencyReportBo {

    @ApiModelProperty(value = "时间搓", notes = "年月/年月日", example = "2024-03/2024/03-01")
    private double timestamp;

    @ApiModelProperty(value = "年月日", notes = "年月日", example = "2024/03-01")
    private LocalDate yearMonthDay;

    @ApiModelProperty(value = "年月", notes = "年月", example = "2024/03")
    private YearMonth yearMonth;

    @ApiModelProperty(value = "出货数")
    private int shippedQuantity;

    @ApiModelProperty(value = "人数")
    private int laborCnt;
}
