package com.hete.supply.scm.server.scm.stockup.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 备货单项目表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("stock_up_order_item")
@ApiModel(value = "StockUpOrderItemPo对象", description = "备货单项目表")
public class StockUpOrderItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "stock_up_order_item_id", type = IdType.ASSIGN_ID)
    private Long stockUpOrderItemId;


    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;


    @ApiModelProperty(value = "SKU")
    private String sku;


    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;


    @ApiModelProperty(value = "回货数")
    private Integer returnGoodsCnt;


    @ApiModelProperty(value = "回货时间")
    private LocalDateTime returnGoodsDate;


}
