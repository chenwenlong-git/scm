package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class PurchaseSettleOrderDetailVo {

    @ApiModelProperty(value = "结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "结算单ID")
    private Long purchaseSettleOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "结算单状态")
    private PurchaseSettleStatus purchaseSettleStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;

    @ApiModelProperty(value = "对账金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "对账人")
    private String confirmUser;

    @ApiModelProperty(value = "对账人名称")
    private String confirmUsername;

    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "供应商确认人")
    private String examineUser;

    @ApiModelProperty(value = "供应商确认人名称")
    private String examineUsername;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "财务审核人")
    private String settleUser;

    @ApiModelProperty(value = "财务审核人名称")
    private String settleUsername;

    @ApiModelProperty(value = "财务审核时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "支付人")
    private String payUser;

    @ApiModelProperty(value = "支付人名称")
    private String payUsername;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "采购结算单明细列表")
    private List<PurchaseSettleOrderItemVo> purchaseSettleOrderItemVoList;

    @ApiModelProperty(value = "支付列表")
    private List<PurchaseSettleOrderPayVo> purchaseSettleOrderPayVoList;

    @Data
    public static class PurchaseSettleOrderItemVo {

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "单据类型")
        private PurchaseSettleItemType purchaseSettleItemType;

        @ApiModelProperty(value = "单据时间")
        private LocalDateTime settleTime;

        @ApiModelProperty(value = "SKU数")
        private Integer skuNum;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "单据状态")
        private String statusName;

    }

    @Data
    public static class PurchaseSettleOrderPayVo {

        @ApiModelProperty(value = "支付ID")
        private Long purchaseSettleOrderPayId;

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

    }


}
