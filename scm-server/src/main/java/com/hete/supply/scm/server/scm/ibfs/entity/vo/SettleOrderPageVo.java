package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@ApiModel(description = "结算单分页搜索结果视图对象")
public class SettleOrderPageVo {
    @ApiModelProperty(value = "结算单号", example = "SETTLE123456")
    private String settleOrderNo;

    @ApiModelProperty(value = "结算状态")
    private FinanceSettleOrderStatus settleOrderStatus;

    @ApiModelProperty(value = "供应商编码（工厂）", example = "SUP123")
    private String supplierCode;

    @ApiModelProperty(value = "创建日期", example = "2024-05-24T10:00:00Z")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "应收总金额", example = "10000.00")
    private BigDecimal totalReceivableAmount;

    @ApiModelProperty(value = "应付总金额", example = "8000.00")
    private BigDecimal totalPayableAmount;

    @ApiModelProperty(value = "结算总金额", example = "9000.00")
    private BigDecimal totalSettleAmount;

    @ApiModelProperty(value = "已付款金额", example = "5000.00")
    private BigDecimal paidAmount;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty("任务id")
    private String taskId;

}
