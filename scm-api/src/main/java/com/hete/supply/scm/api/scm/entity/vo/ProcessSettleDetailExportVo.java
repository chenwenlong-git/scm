package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/18.
 */
@Data
public class ProcessSettleDetailExportVo {

    @ApiModelProperty(value = "结算单明细Id", example = "101")
    private Long processSettleOrderItemId;

    @ApiModelProperty(value = "完成人名称", example = "John Doe")
    private String completeUserName;

    @ApiModelProperty(value = "完成人用户编号", example = "John Doe")
    private String completeUser;

    @ApiModelProperty(value = "个人总提成", example = "1500.00", notes = "结算单明细结算金额")
    private BigDecimal totalCommission;

    @ApiModelProperty(value = "工序编码", example = "类别A")
    private String processCode;

    @ApiModelProperty(value = "工序类别", example = "类别A")
    private String processLabel;

    @ApiModelProperty(value = "工序类别枚举", example = "类别A")
    private ProcessLabel processLabelEnum;

    @ApiModelProperty(value = "工序类别正品数量")
    private Integer processLabelQualityGoodsCnt;

    @ApiModelProperty(value = "工序名称", example = "加工工序")
    private String processName;

    @ApiModelProperty(value = "工序名称正品数量")
    private Integer processNameQualityGoodsCnt;

    @ApiModelProperty(value = "工序基础单价", example = "20.00")
    private BigDecimal processBasePrice;

    @ApiModelProperty(value = "一级系数/临界值", example = "1.5")
    private String firstLevelCoefficientAndLimit;

    @ApiModelProperty(value = "二级系数/临界值", example = "1.5")
    private String secondLevelCoefficientAndLimit;

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
