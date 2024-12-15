package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/3 16:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopChildSearchDto extends ComPageDto {
    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "开发子单号批量")
    private List<String> developChildOrderNoList;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "开发类型(PLM提供SkuDevType枚举)")
    private String skuDevType;

    @ApiModelProperty(value = "开款需求(PLM提供DevelopCreateType枚举)")
    private String developCreateType;

    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    @ApiModelProperty(value = "异常待处理")
    private BooleanType hasException;

    @ApiModelProperty(value = "是否上架")
    private BooleanType isOnShelves;

    @ApiModelProperty(value = "是否加急(PLM提供枚举)")
    private BooleanType isUrgent;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

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

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "是否需要原料")
    private BooleanType isNeedRaw;

    @ApiModelProperty(value = "是否下首单")
    private BooleanType hasFirstOrder;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "打版完成时间")
    private LocalDateTime pamphletCompletionDateStart;

    @ApiModelProperty(value = "打版完成时间")
    private LocalDateTime pamphletCompletionDateEnd;

    @ApiModelProperty(value = "审版完成时间")
    private LocalDateTime reviewCompletionDateStart;

    @ApiModelProperty(value = "审版完成时间")
    private LocalDateTime reviewCompletionDateEnd;

    @ApiModelProperty(value = "核价完成时间")
    private LocalDateTime pricingCompletionDateStart;

    @ApiModelProperty(value = "核价完成时间")
    private LocalDateTime pricingCompletionDateEnd;

    @ApiModelProperty(value = "上新完成时间")
    private LocalDateTime newestCompletionDateStart;

    @ApiModelProperty(value = "上新完成时间")
    private LocalDateTime newestCompletionDateEnd;

    @ApiModelProperty(value = "上架完成时间")
    private LocalDateTime onShelvesCompletionDateStart;

    @ApiModelProperty(value = "上架完成时间")
    private LocalDateTime onShelvesCompletionDateEnd;

    @ApiModelProperty(value = "开发子单状态")
    private DevelopChildOrderStatus developChildOrderStatus;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "开发子单状态")
    private List<DevelopChildOrderStatus> developChildOrderStatusList;

    @ApiModelProperty(value = "版单状态")
    private List<DevelopPamphletOrderStatus> developPamphletOrderStatusList;

    @ApiModelProperty(value = "审版单状态")
    private List<DevelopReviewOrderStatus> developReviewOrderStatusList;

    @ApiModelProperty(value = "核价单状态")
    private List<DevelopPricingOrderStatus> developPricingOrderStatusList;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

}
