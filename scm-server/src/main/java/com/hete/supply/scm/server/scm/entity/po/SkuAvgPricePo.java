package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * sku均价表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_avg_price")
@ApiModel(value = "SkuAvgPricePo对象", description = "sku均价表")
public class SkuAvgPricePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sku_avg_price_id", type = IdType.ASSIGN_ID)
    private Long skuAvgPriceId;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "累积数")
    private Integer accrueCnt;


    @ApiModelProperty(value = "累积总价")
    private BigDecimal accruePrice;


    @ApiModelProperty(value = "sku均价业务类型")
    private SkuAvgPriceBizType skuAvgPriceBizType;


}
