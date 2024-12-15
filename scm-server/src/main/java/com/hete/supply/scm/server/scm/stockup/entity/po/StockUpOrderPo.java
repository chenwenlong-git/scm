package com.hete.supply.scm.server.scm.stockup.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 备货单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("stock_up_order")
@ApiModel(value = "StockUpOrderPo对象", description = "备货单表")
public class StockUpOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "stock_up_order_id", type = IdType.ASSIGN_ID)
    private Long stockUpOrderId;


    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "备货单状态")
    private StockUpOrderStatus stockUpOrderStatus;


    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "分类名(一级或二级名称)")
    private String categoryName;


    @ApiModelProperty(value = "备货单价")
    private BigDecimal stockUpPrice;


    @ApiModelProperty(value = "下单数")
    private Integer placeOrderCnt;


    @ApiModelProperty(value = "要求回货时间")
    private LocalDateTime requestReturnGoodsDate;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


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


}
