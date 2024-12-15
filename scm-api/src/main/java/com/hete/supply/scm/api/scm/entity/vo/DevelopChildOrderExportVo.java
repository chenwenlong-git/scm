package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:15
 */
@Data
@NoArgsConstructor
public class DevelopChildOrderExportVo {

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "开发子单状态")
    private DevelopChildOrderStatus developChildOrderStatus;

    @ApiModelProperty(value = "开发子单状态")
    private String developChildOrderStatusName;

    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private String skuDevTypeName;

    @ApiModelProperty(value = "是否加急(PLM提供枚举)")
    private BooleanType isUrgent;

    @ApiModelProperty(value = "是否加急(PLM提供枚举)")
    private String isUrgentName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商样品报价")
    private String supplierSamplePriceList;

    @ApiModelProperty(value = "供应商大货报价")
    private String supplierBulkPriceList;

    @ApiModelProperty(value = "样品核价")
    private String pricingSamplePriceList;

    @ApiModelProperty(value = "大货核价")
    private String pricingBulkPriceList;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "版单状态")
    private String developPamphletOrderStatusName;

    @ApiModelProperty(value = "样品数量")
    private Integer developSampleNum;

    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "审版单状态")
    private String developReviewOrderStatusName;

    @ApiModelProperty(value = "审版结果")
    private String reviewResultName;

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "核价状态")
    private String developPricingOrderStatusName;

    @ApiModelProperty(value = "产前样单号")
    private String prenatalSampleOrderNo;

    @ApiModelProperty(value = "首单号")
    private String firstSampleOrderNo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "是否上架")
    private BooleanType isOnShelves;

    @ApiModelProperty(value = "是否上架")
    private String isOnShelvesName;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "期望上架时间")
    private String expectedOnShelvesDateStr;

    @ApiModelProperty(value = "上架完成时间")
    private LocalDateTime onShelvesCompletionDate;

    @ApiModelProperty(value = "上架完成时间")
    private String onShelvesCompletionDateStr;

    @ApiModelProperty(value = "开发人")
    private String devUser;

    @ApiModelProperty(value = "开发人")
    private String devUsername;

    @ApiModelProperty(value = "跟单人")
    private String followUser;

    @ApiModelProperty(value = "跟单人")
    private String followUsername;

    @ApiModelProperty(value = "审版人")
    private String reviewUser;

    @ApiModelProperty(value = "审版人")
    private String reviewUsername;

    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUser;

    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUsername;

    @ApiModelProperty(value = "开始打版时间")
    private LocalDateTime pamphletDate;

    @ApiModelProperty(value = "开始打版时间")
    private String pamphletDateStr;

    @ApiModelProperty(value = "完成打版时间")
    private LocalDateTime pamphletCompletionDate;

    @ApiModelProperty(value = "完成打版时间")
    private String pamphletCompletionDateStr;

    @ApiModelProperty(value = "最新样品签收时间")
    private LocalDateTime followDate;

    @ApiModelProperty(value = "最新样品签收时间")
    private String followDateStr;

    @ApiModelProperty(value = "提交审版时间")
    private String submitReviewDateStr;

    @ApiModelProperty(value = "审版完成时间")
    private LocalDateTime reviewCompletionDate;

    @ApiModelProperty(value = "审版完成时间")
    private String reviewCompletionDateStr;

    @ApiModelProperty(value = "提交核价时间")
    private String submitTimeStr;

    @ApiModelProperty(value = "核价完成时间")
    private LocalDateTime pricingCompletionDate;

    @ApiModelProperty(value = "核价完成时间")
    private String pricingCompletionDateStr;

    @ApiModelProperty(value = "上新完成时间")
    private LocalDateTime newestCompletionDate;

    @ApiModelProperty(value = "上新完成时间")
    private String newestCompletionDateStr;

    @ApiModelProperty(value = "产前样下单时间")
    private String prenatalSampleOrderCreateTime;

    @ApiModelProperty(value = "首单下单时间")
    private String firstSampleOrderCreateTime;

    @ApiModelProperty(value = "首单入库时间")
    private String firstSampleOrderWarehousingTime;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "首次样品签收时间")
    private String firstSignTimeStr;

    @ApiModelProperty(value = "采购需求单")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "需求单下单时间")
    private String purchaseParentOrderTime;

}
