package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/18.
 */
@Data
@ApiModel(value = "返修单打印信息结果VO")
public class RepairOrderPrintResultVo {

    @JsonProperty("deliveryNo")
    @ApiModelProperty(value = "出库单号")
    private String outboundOrderNo;

    @ApiModelProperty(value = "返修单id")
    private Long repairOrderId;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "返修单成品收货仓库编码")
    @JsonProperty("warehouseCode")
    private String receivingWarehouseCode;

    @JsonProperty("warehouseName")
    @ApiModelProperty(value = "返修单成品收货仓库名称")
    private String receivingWarehouseName;

    @JsonProperty("deliveryWarehouseCode")
    @ApiModelProperty(value = "返修单原料预计发货仓库编码")
    private String materialShippingWarehouseCode;

    @JsonProperty("deliveryWarehouseName")
    @ApiModelProperty(value = "返修单原料预计发货仓库名称")
    private String materialShippingWarehouseName;

    @JsonProperty("deliverDate")
    @ApiModelProperty(value = "约定日期")
    private LocalDateTime scheduledTime;

    @ApiModelProperty(value = "返修单状态")
    private RepairOrderStatus repairOrderStatus;

    @JsonProperty("createUsername")
    @ApiModelProperty(value = "下单人名称")
    private String orderUsername;

    @JsonProperty("createTime")
    @ApiModelProperty(value = "下单时间")
    private LocalDateTime orderTime;

    @JsonProperty("processOrderNote")
    @ApiModelProperty(value = "计划单备注")
    private String planRemark;

    @JsonProperty("pickOrderNo")
    @ApiModelProperty(value = "拣货单号")
    private String pickingOrderNo;

    @JsonProperty("rawWarehouseCode")
    @ApiModelProperty(value = "出库单仓库编码")
    private String outboundWarehouseCode;

    @JsonProperty("rawWarehouseName")
    @ApiModelProperty(value = "出库单仓库名称")
    private String outboundWarehouseName;

    @JsonProperty("pickingCartStackCodeList")
    @ApiModelProperty(value = "出库单垛口信息")
    private List<String> palletInformation;

    @JsonProperty("processOrderItems")
    @ApiModelProperty(value = "返修产品列表")
    private List<RepairProductInfoVo> repairProductList;

    @JsonProperty("processOrderMaterials")
    @ApiModelProperty(value = "单位原料配比列表")
    private List<UnitMaterialRatioVo> unitMaterialRatioList;

    @JsonProperty("processOrderPrintDeliveryOrderList")
    @ApiModelProperty(value = "出库明细列表")
    private List<OutboundDetailInfoVo> outboundDetailList;

    @Data
    @ApiModel(value = "返修产品信息VO")
    public static class RepairProductInfoVo {
        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "SKU产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "SKU批次码")
        @JsonProperty("skuBatchCode")
        private String batchCode;

        @ApiModelProperty(value = "加工数量")
        @JsonProperty("processNum")
        private Integer processingQuantity;

        @ApiModelProperty(value = "正品数量")
        @JsonProperty("qualityGoodsCnt")
        private Integer goodQuantity;

        @ApiModelProperty(value = "次品数量")
        @JsonProperty("defectiveGoodsCnt")
        private Integer defectiveQuantity;
    }

    @Data
    @ApiModel(value = "单位原料配比信息VO")
    public static class UnitMaterialRatioVo {
        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "SKU编码")
        private String skuEncode;

        @ApiModelProperty(value = "原料配比")
        private Double rate;

        @ApiModelProperty(value = "交付数量")
        private Integer deliveryNum;
    }

    @Data
    @ApiModel(value = "出库明细信息VO")
    public static class OutboundDetailInfoVo {
        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "SKU编码")
        private String skuEncode;

        @ApiModelProperty(value = "出库数量")
        private Integer deliverCnt;
    }
}
