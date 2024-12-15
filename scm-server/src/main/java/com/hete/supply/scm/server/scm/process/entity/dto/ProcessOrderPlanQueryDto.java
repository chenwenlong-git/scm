package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:07
 */
@Data
@ApiModel(value = "产能池查询参数", description = "产能池查询参数")
public class ProcessOrderPlanQueryDto extends ComPageDto {
    @ApiModelProperty(value = "加工单号")
    private Set<String> processOrderNoList;

    @ApiModelProperty(value = "sku列表")
    private List<String> skus;

    @ApiModelProperty(value = "原料sku列表")
    private List<String> materialSkuList;

    @ApiModelProperty(value = "缺失信息")
    private List<MissingInformation> missingInformationList;

    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "是否延误")
    private ProcessPlanDelay processPlanDelay;

    @ApiModelProperty(value = "下单人编号")
    private String createUser;

    @ApiModelProperty(value = "下单人名称")
    private String createUsername;

    @ApiModelProperty(value = "下单时间-起点")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "下单时间-终点")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "排产时间-起点")
    private LocalDateTime processPlanTimeStart;

    @ApiModelProperty(value = "排产时间-终点")
    private LocalDateTime processPlanTimeEnd;

    @ApiModelProperty(value = "预计开始时间-起点（加工单最早一道工序开始时间）")
    private LocalDateTime processPlanEarliestExpectBeginTimeStart;

    @ApiModelProperty(value = "预计开始时间-终点（加工单最早一道工序开始时间）")
    private LocalDateTime processPlanEarliestExpectBeginTimeEnd;

    @ApiModelProperty(value = "预计完成时间-起点（加工单最后一道工序结束时间）")
    private LocalDateTime processPlanLatestExpectEndTimeStart;

    @ApiModelProperty(value = "预计完成时间-终点（加工单最后一道工序结束时间）")
    private LocalDateTime processPlanLatestExpectEndTimeEnd;

    @ApiModelProperty(value = "是否超额")
    private OverPlan overPlan;

    @ApiModelProperty(value = "加工单状态")
    @NotNull(message = "加工单状态不能为空")
    private ProcessOrderStatus processOrderStatus;

    @ApiModelProperty(value = "系统完成时间-起点（加工单最后一道工序结束时间）")
    private LocalDateTime systemProcessPlanLatestExpectEndTimeStart;

    @ApiModelProperty(value = "系统完成时间-终点（加工单最后一道工序结束时间）")
    private LocalDateTime systemProcessPlanLatestExpectEndTimeEnd;

}