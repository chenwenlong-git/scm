package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnSimpleVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 16:17
 */
@Data
@NoArgsConstructor
public class SampleDetailVo {
    @ApiModelProperty(value = "样品需求母单id")
    private Long sampleParentOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;

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

    @ApiModelProperty(value = "是否主动寄样")
    private BooleanType supplySampleType;

    @ApiModelProperty(value = "开发类型")
    private SampleDevType sampleDevType;

    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;

    @ApiModelProperty(value = "卖点描述")
    private String sampleDescribe;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleParentOrderInfoVo> sampleParentOrderInfoList;


    @ApiModelProperty(value = "参照图片")
    private List<String> contrastFileCode;

    @ApiModelProperty(value = "样品需求子单列表")
    private List<SampleChildOrderVo> sampleChildOrderList;

    @ApiModelProperty(value = "次品样品单")
    private String defectiveSampleChildOrderNo;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;

    @ApiModelProperty(value = "素材需求")
    private String sourceMaterial;

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

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "原料bom表")
    private List<SampleRawVo> sampleRawList;

    @ApiModelProperty(value = "样品工序列表")
    private List<SampleParentProcessVo> sampleParentProcessList;
}
