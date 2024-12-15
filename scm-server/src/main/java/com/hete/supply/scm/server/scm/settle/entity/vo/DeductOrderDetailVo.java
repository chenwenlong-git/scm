package com.hete.supply.scm.server.scm.settle.entity.vo;

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

    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "扣款状态")
    private DeductStatus deductStatus;

    @ApiModelProperty(value = "扣款类型")
    private DeductType deductType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "扣款员工")
    private String deductUser;

    @ApiModelProperty(value = "扣款员工名称")
    private String deductUsername;

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

    @ApiModelProperty(value = "支付图片凭证")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "提交人")
    private String submitUser;

    @ApiModelProperty(value = "提交人名称")
    private String submitUsername;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "确认人")
    private String confirmUser;

    @ApiModelProperty(value = "确认人名称")
    private String confirmUsername;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "审核人")
    private String examineUser;

    @ApiModelProperty(value = "审核人名称")
    private String examineUsername;

    @ApiModelProperty(value = "价格拒绝备注")
    private String priceRefuseRemarks;

    @ApiModelProperty(value = "价格确认人的用户")
    private String priceConfirmUser;

    @ApiModelProperty(value = "价格确认人的用户名")
    private String priceConfirmUsername;

    @ApiModelProperty(value = "价格确认时间")
    private LocalDateTime priceConfirmTime;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

    @ApiModelProperty(value = "文件凭证")
    private List<String> documentCodeList;

    @ApiModelProperty(value = "价差扣款明细列表")
    private List<DeductOrderPurchaseVo> deductOrderPurchaseList;

    @ApiModelProperty(value = "加工扣款明细列表")
    private List<DeductOrderProcessVo> deductOrderProcessList;

    @ApiModelProperty(value = "品质扣款明细列表")
    private List<DeductOrderQualityVo> deductOrderQualityList;

    @ApiModelProperty(value = "其他明细列表")
    private List<DeductOrderOtherVo> deductOrderOtherList;

    @ApiModelProperty(value = "预付款明细列表")
    private List<DeductOrderPayVo> deductOrderPayList;

    @ApiModelProperty(value = "次品退供明细列表")
    private List<DeductOrderDefectiveVo> deductOrderDefectiveList;

    @Data
    public static class DeductOrderPurchaseVo {

        @ApiModelProperty(value = "价差扣款明细ID")
        private Long deductOrderPurchaseId;

        @ApiModelProperty(value = "单据类型")
        private DeductOrderPurchaseType deductOrderPurchaseType;

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "SPU")
        private String spu;

        @ApiModelProperty(value = "数量")
        private Integer skuNum;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "结算单价")
        private BigDecimal settleUnitPrice;

    }

    @Data
    public static class DeductOrderProcessVo {

        @ApiModelProperty(value = "加工扣款明细ID")
        private Long deductOrderProcessId;

        @ApiModelProperty(value = "加工单")
        private String processOrderNo;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

    }

    @Data
    public static class DeductOrderQualityVo {

        @ApiModelProperty(value = "品质扣款明细ID")
        private Long deductOrderQualityId;

        @ApiModelProperty(value = "单据类型")
        private DeductOrderPurchaseType deductOrderPurchaseType;

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "SPU")
        private String spu;

        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "数量")
        private Integer skuNum;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

    }

    @Data
    public static class DeductOrderOtherVo {

        @ApiModelProperty(value = "其他明细ID")
        private Long deductOrderOtherId;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

    }

    @Data
    public static class DeductOrderPayVo {

        @ApiModelProperty(value = "预付款明细ID")
        private Long deductOrderPayId;

        @ApiModelProperty(value = "扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "扣款备注")
        private String deductRemarks;

    }

    @Data
    public static class DeductOrderDefectiveVo {

        @ApiModelProperty(value = "id")
        private Long deductOrderDefectiveId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "单据号")
        private String businessNo;


        @ApiModelProperty(value = "sku")
        private String sku;


        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;


        @ApiModelProperty(value = "扣款数量")
        private Integer deductNum;


        @ApiModelProperty(value = "原结算单价")
        private BigDecimal settlePrice;


        @ApiModelProperty(value = "需扣款单价")
        private BigDecimal deductUnitPrice;


        @ApiModelProperty(value = "扣款总价")
        private BigDecimal deductPrice;


        @ApiModelProperty(value = "扣款原因")
        private String deductRemarks;

    }


}
