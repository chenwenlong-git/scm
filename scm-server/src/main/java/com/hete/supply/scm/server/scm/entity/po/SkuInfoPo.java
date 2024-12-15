package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.GoodsPriceMaintain;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * sku关联的业务信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_info")
@ApiModel(value = "SkuInfoPo对象", description = "sku关联的业务信息")
public class SkuInfoPo extends BaseSupplyPo {


    @ApiModelProperty(value = "ID")
    @TableId(value = "sku_info_id", type = IdType.ASSIGN_ID)
    private Long skuInfoId;


    @ApiModelProperty(value = "SKU")
    private String sku;


    @ApiModelProperty(value = "商品价格是否维护")
    private GoodsPriceMaintain goodsPriceMaintain;


    @ApiModelProperty(value = "单件产能")
    private BigDecimal singleCapacity;


}
