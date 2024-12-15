package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/18 23:28
 */
@Data
@NoArgsConstructor
public class SampleDeliverExportVo {
    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

    @ApiModelProperty(value = "样品发货单状态")
    private String sampleDeliverOrderStatus;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


}
