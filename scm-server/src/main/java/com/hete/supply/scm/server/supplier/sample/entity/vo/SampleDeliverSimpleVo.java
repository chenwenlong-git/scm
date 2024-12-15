package com.hete.supply.scm.server.supplier.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/22 10:37
 */
@Data
@NoArgsConstructor
public class SampleDeliverSimpleVo {
    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

    @ApiModelProperty(value = "样品发货单状态")
    private SampleDeliverOrderStatus sampleDeliverOrderStatus;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;
}
