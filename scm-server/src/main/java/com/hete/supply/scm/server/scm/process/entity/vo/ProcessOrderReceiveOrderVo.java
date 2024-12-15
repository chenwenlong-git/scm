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
public class ProcessOrderReceiveOrderVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "入库总数")
    private Integer amount;

    @ApiModelProperty(value = "入库单列表")
    private List<ReceiveOrderVo> receiveOrderVoList;

    @Data
    public static class ReceiveOrderVo {
        @ApiModelProperty(value = "入库单单号")
        private String receiveOrderNo;

        @ApiModelProperty(value = "入库数")
        private Integer receiveAmount;

        @ApiModelProperty(value = "状态")
        private WmsEnum.ReceiveOrderState receiveOrderState;

        @ApiModelProperty(value = "仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "仓库编码")
        private String warehouseCode;
    }


}
