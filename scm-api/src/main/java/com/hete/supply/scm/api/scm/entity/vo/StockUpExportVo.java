package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/1/22 18:02
 */
@Data
@NoArgsConstructor
public class StockUpExportVo {
    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品二级类目")
    private String categoryName;

    @ApiModelProperty(value = "状态")
    private StockUpOrderStatus stockUpOrderStatus;

    @ApiModelProperty(value = "状态")
    private String stockUpOrderStatusStr;

    @ApiModelProperty(value = "预计单价")
    private BigDecimal stockUpPrice;

    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;

    @ApiModelProperty(value = "回货数")
    private Integer returnGoodsCnt;

    @ApiModelProperty(value = "下单数")
    private Integer placeOrderCnt;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "跟单人")
    private String followUsername;

    @ApiModelProperty(value = "跟单时间")
    private LocalDateTime followDate;

    @ApiModelProperty(value = "跟单时间")
    private String followDateStr;

    @ApiModelProperty(value = "完结人")
    private String finishUsername;

    @ApiModelProperty(value = "完结时间")
    private LocalDateTime finishDate;

    @ApiModelProperty(value = "完结时间")
    private String finishDateStr;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

}
