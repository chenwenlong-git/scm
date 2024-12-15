package com.hete.supply.scm.server.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.supplier.enums.ShippingMarkBizType;
import com.hete.supply.scm.server.supplier.enums.ShippingMarkStatus;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 箱唛表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("shipping_mark")
@ApiModel(value = "ShippingMarkPo对象", description = "箱唛表")
public class ShippingMarkPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "shipping_mark_id", type = IdType.ASSIGN_ID)
    private Long shippingMarkId;


    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;


    @ApiModelProperty(value = "箱唛状态")
    private ShippingMarkStatus shippingMarkStatus;


    @ApiModelProperty(value = "箱唛业务类型")
    private ShippingMarkBizType shippingMarkBizType;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;


    @ApiModelProperty(value = "仓库类型")
    private String warehouseTypes;

    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;


    @ApiModelProperty(value = "箱数")
    private Integer boxCnt;


    @ApiModelProperty(value = "发货人id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;


}
