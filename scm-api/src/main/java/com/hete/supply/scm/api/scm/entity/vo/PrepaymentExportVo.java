package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/5/25 16:25
 */
@Data
@NoArgsConstructor
public class PrepaymentExportVo {
    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;

    @ApiModelProperty(value = "预付款状态")
    private PrepaymentOrderStatus prepaymentOrderStatus;

    @ApiModelProperty(value = "预付款状态")
    private String prepaymentOrderStatusStr;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "关联飞书审批单号")
    private String workflowNo;

    @ApiModelProperty(value = "预付类型")
    private PrepaymentType prepaymentType;

    @ApiModelProperty(value = "预付类型")
    private String prepaymentTypeStr;

    @ApiModelProperty(value = "预付事由")
    private String prepaymentReason;

    @ApiModelProperty(value = "预付金额")
    private BigDecimal prepaymentMoney;

    @ApiModelProperty(value = "预付备注")
    private String prepaymentRemark;


}
