package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/10 13:56
 */
@Data
@NoArgsConstructor
public class ReceiveOrderPrintVo {
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应链单据号")
    private String scmBizNo;

    @ApiModelProperty(value = "总发货数量")
    private Integer deliveryAmount;

    @ApiModelProperty(value = "收货类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发货单明细列表")
    private List<ReceiveDeliver> receiveDeliverList;

    @Data
    public static class ReceiveDeliver {

        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "发货数量")
        private Integer deliveryAmount;

        @ApiModelProperty(value = "收货数量")
        private Integer receiveAmount;
    }
}
