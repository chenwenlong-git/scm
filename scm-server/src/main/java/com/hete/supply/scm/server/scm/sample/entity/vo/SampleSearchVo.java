package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 15:59
 */
@Data
@NoArgsConstructor
public class SampleSearchVo {
    @ApiModelProperty(value = "样品需求母单id")
    private Long sampleParentOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;


    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;


    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;


    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;


    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "采购总数")
    private Integer purchaseTotal;

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


    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "开款人")
    private String disburseUser;

    @ApiModelProperty(value = "开款人")
    private String disburseUsername;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @ApiModelProperty(value = "参照图片")
    private String contrastFileUrl;
}
