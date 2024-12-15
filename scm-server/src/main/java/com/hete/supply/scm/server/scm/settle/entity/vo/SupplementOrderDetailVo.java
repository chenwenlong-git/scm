package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplementOrderPurchaseType;
import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import io.swagger.annotations.ApiModel;
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
public class SupplementOrderDetailVo {

    @ApiModelProperty(value = "补款ID")
    private Long supplementOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "补款状态")
    private SupplementStatus supplementStatus;

    @ApiModelProperty(value = "补款类型")
    private SupplementType supplementType;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "补款员工")
    private String supplementUser;

    @ApiModelProperty(value = "补款员工名称")
    private String supplementUsername;

    @ApiModelProperty(value = "补款总金额")
    private BigDecimal supplementPrice;

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

    @ApiModelProperty(value = "创建人")
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

    @ApiModelProperty(value = "价差补款明细列表")
    private List<SupplementOrderPurchaseVo> supplementOrderPurchaseList;

    @ApiModelProperty(value = "加工补款明细列表")
    private List<SupplementOrderProcessVo> supplementOrderProcessList;

    @ApiModelProperty(value = "其他明细列表")
    private List<SupplementOrderOtherVo> supplementOrderOtherList;

    @Data
    @ApiModel(value = "价差补款明细")
    public static class SupplementOrderPurchaseVo {

        @ApiModelProperty(value = "价差补款明细ID")
        private Long supplementOrderPurchaseId;

        @ApiModelProperty(value = "单据类型")
        private SupplementOrderPurchaseType supplementOrderPurchaseType;

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "SPU")
        private String spu;

        @ApiModelProperty(value = "数量")
        private Integer skuNum;

        @ApiModelProperty(value = "补款金额")
        private BigDecimal supplementPrice;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "补款备注")
        private String supplementRemarks;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "结算单价")
        private BigDecimal settleUnitPrice;

    }

    @Data
    @ApiModel(value = "加工补款明细")
    public static class SupplementOrderProcessVo {

        @ApiModelProperty(value = "加工补款明细ID")
        private Long supplementOrderProcessId;

        @ApiModelProperty(value = "加工单号")
        private String processOrderNo;

        @ApiModelProperty(value = "补款金额")
        private BigDecimal supplementPrice;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "补款备注")
        private String supplementRemarks;

    }

    @Data
    @ApiModel(value = "其他明细")
    public static class SupplementOrderOtherVo {

        @ApiModelProperty(value = "其他明细ID")
        private Long supplementOrderOtherId;

        @ApiModelProperty(value = "补款金额")
        private BigDecimal supplementPrice;

        @ApiModelProperty(value = "补款备注")
        private String supplementRemarks;

    }


}
