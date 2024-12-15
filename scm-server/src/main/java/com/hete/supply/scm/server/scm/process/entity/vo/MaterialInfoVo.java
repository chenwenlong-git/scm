package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author yanjiawei
 * Created on 2024/7/23.
 */
@Data
public class MaterialInfoVo {

    @ApiModelProperty(value = "原料SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "计划仓库发货数")
    private Integer planDeliveryNum;

    @ApiModelProperty(value = "加工收货数")
    private Integer processReceiveNum = 0;

    @ApiModelProperty(value = "归还入库数")
    private Integer backReceiveNum = 0;

    @ApiModelProperty(value = "原料批次码收货信息列表")
    private List<MaterialReceiveInfoVo> materialReceiveInfoVoList = emptyList();

    @Data
    public static class MaterialReceiveInfoVo {
        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "原料批次码出库信息列表")
        private List<MaterialDeliveryInfoVo> materialDeliveryInfoVoList = emptyList();

        @ApiModelProperty(value = "原料归还信息列表")
        private List<MaterialBackInfoVo> materialBackInfoVoList = emptyList();
    }

    @Data
    public static class MaterialDeliveryInfoVo {
        @ApiModelProperty(value = "出库单号")
        private String deliveryNo;

        @ApiModelProperty(value = "原料需求仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "原料需求仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "出库数量")
        private Integer deliveryNum;

        @ApiModelProperty(value = "收货数量")
        private Integer receiveNum;

        @ApiModelProperty(value = "出库单迁出状态")
        private WmsEnum.DeliveryState deliveryState;
    }

    @Data
    public static class MaterialBackInfoVo {
        @ApiModelProperty(value = "收货单号")
        private String receiveNo;

        @ApiModelProperty(value = "收货仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "收货仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "收货数量")
        private Integer receiveNum;

        @ApiModelProperty(value = "收货状态")
        private WmsEnum.ReceiveOrderState receiveOrderState;
    }

}









