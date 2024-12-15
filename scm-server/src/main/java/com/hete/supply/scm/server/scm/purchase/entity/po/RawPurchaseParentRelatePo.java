package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购子单与原料采购母单关联表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("raw_purchase_parent_relate")
@ApiModel(value = "RawPurchaseParentRelatePo对象", description = "采购子单与原料采购母单关联表")
public class RawPurchaseParentRelatePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "raw_purchase_parent_relate_id", type = IdType.ASSIGN_ID)
    private Long rawPurchaseParentRelateId;


    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "原料采购母单单号")
    private String rawPurchaseParentOrderNo;


}
