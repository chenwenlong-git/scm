package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/17.
 */
@Data
@ApiModel(description = "结算单明细与扫码明细")
public class ProcessSettleOrderDetailAndScanSettleVo {
    // 结算单明细信息

    @ApiModelProperty(value = "结算单id", example = "1")
    private Long processSettleOrderId;

    @ApiModelProperty(value = "结算单明细Id", example = "101")
    private Long processSettleOrderItemId;

    @ApiModelProperty(value = "完成人编号", example = "1001")
    private String completeUser;

    @ApiModelProperty(value = "完成人名称", example = "John Doe")
    private String completeUserName;

    @ApiModelProperty(value = "个人总提成", example = "1500.00", notes = "结算单明细结算金额")
    private BigDecimal totalCommission;

    // 扫码结算信息

    @ApiModelProperty(value = "扫码结算单id", example = "101")
    private Long processSettleOrderScanId;

    @ApiModelProperty(value = "工序编码", example = "PROC001")
    private String processCode;

    @ApiModelProperty(value = "工序类别", example = "类别A")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序名称", example = "加工工序")
    private String processName;

    @ApiModelProperty(value = "工序类别", example = "组合/独立")
    private ProcessType processType;

    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "工序基础单价", example = "20.00")
    private BigDecimal processBasePrice;

    @ApiModelProperty(value = "一级系数", example = "1.5")
    private BigDecimal firstLevelCoefficient;

    @ApiModelProperty(value = "一级起始值", example = "10")
    private Integer firstLevelStartQuantity;

    @ApiModelProperty(value = "一级结束值", example = "20")
    private Integer firstLevelEndQuantity;

    @ApiModelProperty(value = "二级系数", example = "2.0")
    private BigDecimal secondLevelCoefficient;

    @ApiModelProperty(value = "二级起始值", example = "21")
    private Integer secondLevelStartQuantity;

    @ApiModelProperty(value = "二级结束值", example = "30")
    private Integer secondLevelEndQuantity;

    @ApiModelProperty(value = "一级产品数", example = "15")
    private Integer firstLevelQualityGoodsCnt;

    @ApiModelProperty(value = "一级提成", example = "225.00")
    private BigDecimal firstLevelTotalCommission;

    @ApiModelProperty(value = "二级产品数", example = "10")
    private Integer secondLevelQualityGoodsCnt;

    @ApiModelProperty(value = "二级提成", example = "200.00")
    private BigDecimal secondLevelTotalCommission;

    @ApiModelProperty(value = "补款金额", example = "50.00")
    private BigDecimal additionalPayment;

    @ApiModelProperty(value = "扣款金额", example = "10.00")
    private BigDecimal deductionAmount;

}
