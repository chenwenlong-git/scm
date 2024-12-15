package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author RockyHuas
 * @date 2022/11/15 10:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderScanVo {

    @ApiModelProperty(value = "id")
    private Long processOrderScanId;


    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;


    @ApiModelProperty(value = "关联的工序代码")
    private String processCode;


    @ApiModelProperty(value = "关联的工序名称")
    private String processName;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序类型")
    private ProcessType processType;

    @ApiModelProperty(value = "关联的二级工序名称")
    private String processSecondName;


    @ApiModelProperty(value = "关联的工序提成", notes = "关联的工序提成变成基础单价")
    private BigDecimal processCommission;

    @ApiModelProperty(value = "接货数量")
    private Integer receiptNum;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "下单时间")
    private LocalDateTime orderTime;


    @ApiModelProperty(value = "下单人名称")
    private String orderUsername;


    @ApiModelProperty(value = "接货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "接货人")
    private String receiptUser;

    @ApiModelProperty(value = "接货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "扫码创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;

    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "一级提成总金额", example = "225.00")
    private BigDecimal firstLevelCommissionAmount;

    @ApiModelProperty(value = "二级提成总金额", example = "200.00")
    private BigDecimal secondLevelCommissionAmount;

    @ApiModelProperty(value = "总提成", example = "200.00")
    private BigDecimal totalCommissionAmount;

    @ApiModelProperty(value = "工序状态")
    private ProcessProgressStatus processProgressStatus;
}
