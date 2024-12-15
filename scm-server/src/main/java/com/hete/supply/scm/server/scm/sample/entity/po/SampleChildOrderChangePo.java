package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 样品需求子单变更记录
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_child_order_change")
@ApiModel(value = "SampleChildOrderChangePo对象", description = "样品需求子单变更记录")
public class SampleChildOrderChangePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_child_order_change_id", type = IdType.ASSIGN_ID)
    private Long sampleChildOrderChangeId;


    @ApiModelProperty(value = "样品采购子单id")
    private Long sampleChildOrderId;


    @ApiModelProperty(value = "发货单号")
    private String sampleDeliverOrderNo;


    @ApiModelProperty(value = "收货单号")
    private String sampleReceiptOrderNo;


    @ApiModelProperty(value = "质检单号")
    private String sampleQcOrderNo;


    @ApiModelProperty(value = "入库单号")
    private String sampleWarehousingOrderNo;


    @ApiModelProperty(value = "结算单号")
    private String sampleSettleOrderNo;


    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;


    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;


    @ApiModelProperty(value = "审核人")
    private String approveUser;


    @ApiModelProperty(value = "审核人")
    private String approveUsername;


    @ApiModelProperty(value = "接单人")
    private String receiveUser;


    @ApiModelProperty(value = "接单人")
    private String receiveUsername;


    @ApiModelProperty(value = "收货人")
    private String receiptUser;


    @ApiModelProperty(value = "收货人")
    private String receiptUsername;


    @ApiModelProperty(value = "选样人")
    private String sampleUser;


    @ApiModelProperty(value = "选样人")
    private String sampleUsername;

    @ApiModelProperty(value = "发货人")
    private String deliverUser;

    @ApiModelProperty(value = "发货人")
    private String deliverUsername;


    @ApiModelProperty(value = "下发打版时间")
    private LocalDateTime typesetTime;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTime;


    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "确认打版时间")
    private LocalDateTime typesettingTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;


}
