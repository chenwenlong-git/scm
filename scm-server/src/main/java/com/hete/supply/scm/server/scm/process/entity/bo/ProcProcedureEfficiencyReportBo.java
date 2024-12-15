package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @author yanjiawei
 * Created on 2024/3/13.
 */
@Data
@ApiModel(description = "加工工序人效报表（日）")
public class ProcProcedureEfficiencyReportBo implements Serializable {

    @ApiModelProperty(value = "时间搓", notes = "年月/年月日", example = "2024-03/2024/03-01")
    private double timestamp;

    private String dateStr;

    @ApiModelProperty(value = "年月日", notes = "年月日", example = "2024/03-01")
    private LocalDate yearMonthDay;

    @ApiModelProperty(value = "年月", notes = "年月", example = "2024/03")
    private YearMonth yearMonth;

    @ApiModelProperty(notes = "修剪蕾丝数量")
    private int trimmingLaceCount = 0;

    @ApiModelProperty(notes = "修剪蕾丝人力")
    private int trimmingLaceManpower = 2;

    @ApiModelProperty(notes = "造型组造型数量")
    private int stylingTeamStylingCount = 0;

    @ApiModelProperty(notes = "造型组造型人力")
    private int stylingTeamStylingManpower;


    @ApiModelProperty(notes = "染色组造型数量")
    private int coloringTeamStylingCount = 0;

    @ApiModelProperty(notes = "染色组造型人力")
    private int coloringTeamStylingManpower = 0;

    @ApiModelProperty(notes = "染色数量")
    private int coloringCount = 0;

    @ApiModelProperty(notes = "染色人力")
    private int coloringManpower = 0;

    @ApiModelProperty(notes = "卡子+松紧带数量")
    private int clipsElasticBandsCount = 0;

    @ApiModelProperty(notes = "卡子+松紧带人力")
    private int clipsElasticBandsManpower = 1;

    @ApiModelProperty(notes = "缝头套数量")
    private int sewingHeadbandsCount = 0;

    @ApiModelProperty(notes = "缝头套人力")
    private int sewingHeadbandsManpower = 0;
}
