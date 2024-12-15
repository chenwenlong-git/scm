package com.hete.supply.scm.server.supplier.entity.po;

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
 * 箱唛分箱表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("shipping_mark_item")
@ApiModel(value = "ShippingMarkItemPo对象", description = "箱唛分箱表")
public class ShippingMarkItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "shipping_mark_item_id", type = IdType.ASSIGN_ID)
    private Long shippingMarkItemId;


    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;


    @ApiModelProperty(value = "箱唛箱号（序号）")
    private String shippingMarkNum;


    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;


    @ApiModelProperty(value = "业务子单单号")
    private String bizChildOrderNo;


    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


}
