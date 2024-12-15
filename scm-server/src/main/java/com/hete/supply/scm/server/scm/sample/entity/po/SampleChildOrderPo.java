package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 样品需求子单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_child_order")
@ApiModel(value = "SampleChildOrderPo对象", description = "样品需求子单")
public class SampleChildOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_child_order_id", type = IdType.ASSIGN_ID)
    private Long sampleChildOrderId;


    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;


    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;


    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "需求描述")
    private String demandDescribe;


    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "选样结果")
    private SampleResult sampleResult;


    @ApiModelProperty(value = "打样单价")
    private BigDecimal proofingPrice;


    @ApiModelProperty(value = "卖点描述")
    private String sampleDescribe;


    @ApiModelProperty(value = "样品改善要求")
    private String sampleImprove;

    @ApiModelProperty(value = "供应商生产信息")
    private String supplierProduction;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "成品入仓收货单号")
    private String sampleReceiptOrderNo;

    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "开发类型")
    private SampleDevType sampleDevType;

    @ApiModelProperty(value = "生产标签")
    private SampleProduceLabel sampleProduceLabel;
}
