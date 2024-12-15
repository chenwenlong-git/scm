package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/5/24 14:34
 */
@Data
@NoArgsConstructor
public class WmsReceiveDetailVo {
    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "收货单状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime finishReceiveTime;

    @ApiModelProperty(value = "收货数量")
    private Integer receiveAmount;


}
