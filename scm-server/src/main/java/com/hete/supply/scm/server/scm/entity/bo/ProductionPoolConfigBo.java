package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.process.entity.bo.BreakTimeSegmentBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * @author yanjiawei
 * @date 2023年08月17日 15:18
 */
@ApiModel(description = "产能池配置")
@Data
public class ProductionPoolConfigBo {
    @ApiModelProperty(value = "产能池编号")
    private String productionPoolCode;

    @ApiModelProperty(value = "订单类型列表")
    private Set<ProcessOrderType> orderTypes;

    @ApiModelProperty(value = "员工编号列表")
    private Set<String> employeeNos;

    @ApiModelProperty(value = "排产周期（天）")
    private Integer productionCycle;

    @ApiModelProperty(value = "上班时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "下班时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "节假日列表")
    private Set<LocalDate> holidays;

    @ApiModelProperty(value = "休息时间段列表")
    private List<BreakTimeSegmentBo> breakTimeSegments;

    @ApiModelProperty(value = "工作时长（小时）")
    private BigDecimal workHourDuration;

    @ApiModelProperty(value = "最大排产单量")
    private Long maxProductionOrders;

}
