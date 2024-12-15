package com.hete.supply.scm.server.supplier.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DeductOrderPurchaseType;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
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
public class DeductOrderDetailVo {

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "扣款状态")
    private DeductStatus deductStatus;

    @ApiModelProperty(value = "扣款类型")
    private DeductType deductType;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "扣款总金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "关联结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;

    @ApiModelProperty(value = "确认拒绝原因")
    private String confirmRefuseRemarks;

    @ApiModelProperty(value = "审核拒绝原因")
    private String examineRefuseRemarks;

    @ApiModelProperty(value = "支付凭证")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "提交人")
    private String submitUsername;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "审核人")
    private String examineUsername;

    @ApiModelProperty(value = "价差扣款明细列表")
    private List<DeductOrderPurchase> deductOrderPurchase;

    @ApiModelProperty(value = "加工扣款明细列表")
    private List<DeductOrderProcess> deductOrderProcess;

    @Data
    public static class DeductOrderPurchase {

        @ApiModelProperty(value = "价差扣款明细ID")
        private Long deductOrderPurchaseId;

        @ApiModelProperty(value = "单据类型")
        private DeductOrderPurchaseType deductOrderPurchaseType;

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "SKU/SPU")
        private String sku;

        @ApiModelProperty(value = "数量")
        private Integer skuNum;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

    }

    @Data
    public static class DeductOrderProcess {

        @ApiModelProperty(value = "加工扣款明细ID")
        private Long deductOrderProcessId;

        @ApiModelProperty(value = "加工单")
        private String processOrderNo;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

    }


}
