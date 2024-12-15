package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * sku属性定价表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_attr_price")
@ApiModel(value = "SkuAttrPricePo对象", description = "sku属性定价表")
public class SkuAttrPricePo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "sku_attr_price_id", type = IdType.ASSIGN_ID)
    private Long skuAttrPriceId;

    @ApiModelProperty(value = "蕾丝面积属性值")
    private String laceAttrValue;


    @ApiModelProperty(value = "档长尺寸属性值")
    private String sizeAttrValue;

    @ApiModelProperty(value = "材料属性值")
    private String materialAttrValue;


    @ApiModelProperty(value = "sku价格")
    private BigDecimal skuPrice;

}
