package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/1/3 10:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WmsCancelDeliverMqDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "业务单号")
    private String scmBizNo;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人")
    private String operatorName;

}
