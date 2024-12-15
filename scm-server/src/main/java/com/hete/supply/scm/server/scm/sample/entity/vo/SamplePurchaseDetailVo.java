package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import com.hete.supply.scm.server.scm.entity.vo.RawSampleItemVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnSimpleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 22:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SamplePurchaseDetailVo extends SupplierSimpleVo {

    @ApiModelProperty(value = "id")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品单状态(母单)")
    private SampleOrderStatus sampleOrderStatus;

    @ApiModelProperty(value = "样品单状态(子单)")
    private SampleOrderStatus sampleChildOrderStatus;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;

    @ApiModelProperty(value = "打样单价")
    private BigDecimal proofingPrice;

    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "卖点描述")
    private String sampleDescribe;

    @ApiModelProperty(value = "样品改善要求")
    private String sampleImprove;

    @ApiModelProperty(value = "重新打样次数")
    private Integer reSampleTimes;

    @ApiModelProperty(value = "需求描述")
    private String demandDescribe;

    @ApiModelProperty(value = "供应商生产信息")
    private String supplierProduction;

    @ApiModelProperty(value = "素材需求")
    private String sourceMaterial;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "选中样实拍图")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "参照图片")
    private List<String> contrastFileCode;

    @ApiModelProperty(value = "样品改善图片")
    private List<String> reSampleFileCode;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;

    @ApiModelProperty(value = "样品需求子单列表")
    private List<SampleChildOrderVo> sampleChildOrderList;

    @ApiModelProperty(value = "样品发货信息")
    private List<SampleDeliverSimpleVo> sampleDeliverSimpleList;

    @ApiModelProperty(value = "样品收货信息")
    private List<SampleReceiptSimpleVo> sampleReceiptSimpleList;

    @ApiModelProperty(value = "样品退货信息")
    private List<SampleReturnSimpleVo> sampleReturnSimpleVoList;

    @ApiModelProperty(value = "结算信息")
    private List<PurchaseSettleSimpleVo> purchaseSettleSimpleList;

    @ApiModelProperty(value = "选样结果信息")
    private List<SampleResultVo> sampleResultList;

    @ApiModelProperty(value = "样品工序列表")
    private List<SampleProcessVo> sampleProcessList;

    @ApiModelProperty(value = "样品工序描述列表")
    private List<SampleProcessDescVo> sampleProcessDescList;

    @ApiModelProperty(value = "原料bom表")
    private List<SampleRawVo> sampleRawList;

    @ApiModelProperty(value = "原料明细列表")
    private List<RawSampleItemVo> rawSampleItemList;

    @ApiModelProperty(value = "开发类型")
    private SampleDevType sampleDevType;

    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;
}
