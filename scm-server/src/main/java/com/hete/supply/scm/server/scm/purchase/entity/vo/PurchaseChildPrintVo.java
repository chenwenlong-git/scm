package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseChildPrintVo extends SupplierSimpleVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "打印人")
    private String printUsername;

    @ApiModelProperty(value = "打印时间")
    private LocalDateTime printTime;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "采购子单明细项列表")
    private List<ChildOrderPurchaseItem> purchaseProductItemList;

    @ApiModelProperty(value = "生产信息")
    private List<SampleInfoVo> sampleInfoVoList;

    @Data
    public static class ChildOrderPurchaseItem {

        @ApiModelProperty(value = "id")
        private Long purchaseChildOrderItemId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "采购数")
        private Integer purchaseCnt;

        @ApiModelProperty(value = "打印图片")
        private List<String> fileCodeList;

    }

}
