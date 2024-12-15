package com.hete.supply.scm.server.scm.stockup.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 19:49
 */
@Data
@NoArgsConstructor
public class StockUpSearchVo {
    @ApiModelProperty(value = "id")
    private Long stockUpOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku主图")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "分类名(一级或二级名称)")
    private String categoryName;

    @ApiModelProperty(value = "备货单状态")
    private StockUpOrderStatus stockUpOrderStatus;

    @ApiModelProperty(value = "备货单价")
    private BigDecimal stockUpPrice;

    @ApiModelProperty(value = "下单数")
    private Integer placeOrderCnt;

    @ApiModelProperty(value = "备货明细列表")
    private List<StockUpSearchItemVo> stockUpSearchItemList;

    @ApiModelProperty(value = "要求回货时间")
    private LocalDateTime requestReturnGoodsDate;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "跟单人")
    private String followUser;


    @ApiModelProperty(value = "跟单人")
    private String followUsername;

    @ApiModelProperty(value = "跟单备注")
    private String followRemark;


    @ApiModelProperty(value = "跟单时间")
    private LocalDateTime followDate;


    @ApiModelProperty(value = "完结人")
    private String finishUser;


    @ApiModelProperty(value = "完结人")
    private String finishUsername;


    @ApiModelProperty(value = "完结时间")
    private LocalDateTime finishDate;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;


}
