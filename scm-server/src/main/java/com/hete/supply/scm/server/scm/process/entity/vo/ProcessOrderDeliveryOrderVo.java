package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/3/14 10:52
 */
@Data
@NoArgsConstructor
public class ProcessOrderDeliveryOrderVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数")
    private Integer amount;

    @ApiModelProperty(value = "收货数")
    private Integer receiptNum;

    @ApiModelProperty(value = "出库单列表")
    private List<DeliveryOrderVo> deliveryOrderVoList;

    @Data
    public static class DeliveryOrderVo {
        @ApiModelProperty(value = "出库单单号")
        private String deliveryOrderNo;

        @ApiModelProperty(value = "数量")
        private Integer deliveryAmount;

        @ApiModelProperty(value = "状态")
        private WmsEnum.DeliveryState deliveryState;

        @ApiModelProperty(value = "仓库名称")
        private String warehouseName;
    }


}
