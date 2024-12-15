package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/8 11:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RepairOrderSearchDto extends ComPageDto {
    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "计划单号")
    private String planNo;

    @ApiModelProperty(value = "下单人用户")
    private String planCreateUser;

    @ApiModelProperty(value = "下单人用户名称")
    private String planCreateUsername;

    @ApiModelProperty(value = "标题")
    private String planTitle;

    @ApiModelProperty(value = "需求平台编码")
    private String platform;

    @ApiModelProperty(value = "是否回料枚举（是/否）")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "下单时间起始值")
    private LocalDateTime planCreateTimeStart;

    @ApiModelProperty(value = "下单时间结束值")
    private LocalDateTime planCreateTimeEnd;

    @ApiModelProperty(value = "返修完成时间起始值")
    private LocalDateTime repairCompleteTimeStart;

    @ApiModelProperty(value = "返修完成时间结束值")
    private LocalDateTime repairCompleteTimeEnd;

    @ApiModelProperty(value = "期望上架时间起始值")
    private LocalDateTime expectCompleteProcessTimeStart;

    @ApiModelProperty(value = "期望上架时间结束值")
    private LocalDateTime expectCompleteProcessTimeEnd;

    @ApiModelProperty(value = "返修单状态")
    private List<RepairOrderStatus> repairOrderStatusList;

    @ApiModelProperty(value = "返修单号批量")
    private List<String> repairOrderNoList;

    @ApiModelProperty(value = "计划单号批量")
    private List<String> planNoList;

}
