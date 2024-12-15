package com.hete.supply.scm.server.supplier.sample.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 12:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleDeliverListDto extends ComPageDto {

    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "发货人id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货单状态")
    private List<DeliverOrderStatus> deliverOrderStatusList;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "spu")
    private String spu;
}
