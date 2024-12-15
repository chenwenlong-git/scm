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
 * 海外仓信息表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("overseas_warehouse_msg")
@ApiModel(value = "OverseasWarehouseMsgPo对象", description = "海外仓信息表")
public class OverseasWarehouseMsgPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "overseas_warehouse_msg_id", type = IdType.ASSIGN_ID)
    private Long overseasWarehouseMsgId;

    @ApiModelProperty(value = "海外仓箱唛号")
    private String overseasShippingMarkNo;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;
}
