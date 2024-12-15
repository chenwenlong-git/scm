package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 15:57
 */
@Data
@NoArgsConstructor
public class DevelopSampleSettleOrderDetailVo {

    @ApiModelProperty(value = "结算单ID")
    private Long developSampleSettleOrderId;

    @ApiModelProperty(value = "结算单号")
    private String developSampleSettleOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "结算单状态")
    private DevelopSampleSettleStatus developSampleSettleStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "对账金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "已支付金额")
    private BigDecimal paidPrice;

    @ApiModelProperty(value = "待支付金额")
    private BigDecimal waitPayPrice;

    @ApiModelProperty(value = "样品结算单明细列表")
    private List<DevelopSampleSettleOrderItemVo> developSampleSettleOrderItemVoList;

    @ApiModelProperty(value = "支付列表")
    private List<DevelopSampleSettleOrderPayVo> developSampleSettleOrderPayVoList;

    @Data
    public static class DevelopSampleSettleOrderItemVo {

        @ApiModelProperty(value = "关联单据号")
        private String businessNo;

        @ApiModelProperty(value = "上架时间")
        private LocalDateTime settleTime;

        @ApiModelProperty(value = "开发子单号")
        private String developChildOrderNo;

        @ApiModelProperty(value = "样品单号")
        private String developSampleOrderNo;

        @ApiModelProperty(value = "SKU数")
        private Integer skuNum;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal samplePrice;

        @ApiModelProperty(value = "处理方式")
        private DevelopSampleMethod developSampleMethod;


    }

    @Data
    public static class DevelopSampleSettleOrderPayVo {

        @ApiModelProperty(value = "支付ID")
        private Long developSampleSettlePayId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "交易号")
        private String transactionNo;

        @ApiModelProperty(value = "支付时间")
        private LocalDateTime payTime;

        @ApiModelProperty(value = "支付金额")
        private BigDecimal payPrice;

        @ApiModelProperty(value = "支付凭证")
        private List<String> fileCodeList;

        @ApiModelProperty(value = "单据时间")
        private LocalDateTime settleTime;

        @ApiModelProperty(value = "SKU数")
        private Integer skuNum;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "备注")
        private String remarks;

    }
}
