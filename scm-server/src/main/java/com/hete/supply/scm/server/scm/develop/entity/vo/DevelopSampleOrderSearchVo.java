package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleType;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:15
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderSearchVo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "状态")
    private DevelopSampleStatus developSampleStatus;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "关联单据号")
    private String receiptOrderNo;

    @ApiModelProperty(value = "上架时间")
    private LocalDateTime shelvesTime;

    @ApiModelProperty(value = "样品结算单")
    private String developSampleSettleOrderNo;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "寄样时间")
    private LocalDateTime finishPamphletDate;

    @ApiModelProperty(value = "打样样图")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "样品单类型")
    private DevelopSampleType developSampleType;

    @ApiModelProperty(value = "批次码样品价格")
    private BigDecimal skuBatchSamplePrice;

    @ApiModelProperty(value = "退样运单号")
    private String returnTrackingNo;

    @ApiModelProperty(value = "货物走向")
    private DevelopSampleDirection developSampleDirection;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "渠道大货报价列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;


}
