package com.hete.supply.scm.server.scm.adjust.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品价格审批单记录
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_price_approve")
@ApiModel(value = "GoodsPriceApprovePo对象", description = "商品价格审批单记录")
public class GoodsPriceApprovePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "goods_price_approve_id", type = IdType.ASSIGN_ID)
    private Long goodsPriceApproveId;


    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


}
