package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/3/14 10:52
 */
@Data
@NoArgsConstructor
public class ProcessOrderCompleteReceiveOrderVo {
    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "数量")
    private Integer amount;


}
