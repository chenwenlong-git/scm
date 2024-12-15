package com.hete.supply.scm.server.supplier.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 样品发货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_deliver_order")
@ApiModel(value = "SampleDeliverOrderPo对象", description = "样品发货单")
public class SampleDeliverOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_deliver_order_id", type = IdType.ASSIGN_ID)
    private Long sampleDeliverOrderId;


    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;


    @ApiModelProperty(value = "样品发货单状态")
    private SampleDeliverOrderStatus sampleDeliverOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;


    @ApiModelProperty(value = "发货人id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "是否生成箱唛")
    private BooleanType hasShippingMark;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;
}
