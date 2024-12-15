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
 * 样品需求母单变更记录
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_parent_order_change")
@ApiModel(value = "SampleParentOrderChangePo对象", description = "样品需求母单变更记录")
public class SampleParentOrderChangePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_parent_order_change_id", type = IdType.ASSIGN_ID)
    private Long sampleParentOrderChangeId;


    @ApiModelProperty(value = "样品采购母单id")
    private Long sampleParentOrderId;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTime;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;


    @ApiModelProperty(value = "开款时间")
    private LocalDateTime disbursementTime;


    @ApiModelProperty(value = "确认打版时间")
    private LocalDateTime typesettingTime;


    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;


    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;


    @ApiModelProperty(value = "审核人")
    private String approveUser;


    @ApiModelProperty(value = "审核人")
    private String approveUsername;


    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;


    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;


    @ApiModelProperty(value = "开款人")
    private String disburseUser;


    @ApiModelProperty(value = "开款人")
    private String disburseUsername;


}
