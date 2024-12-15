package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.support.api.enums.BooleanType;
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
 * 样品需求母单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_parent_order")
@ApiModel(value = "SampleParentOrderPo对象", description = "样品需求母单")
public class SampleParentOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_parent_order_id", type = IdType.ASSIGN_ID)
    private Long sampleParentOrderId;


    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;


    @ApiModelProperty(value = "开发类型")
    private SampleDevType sampleDevType;


    @ApiModelProperty(value = "商品类目")
    private String categoryName;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;


    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "是否主动寄样")
    private BooleanType supplySampleType;

    @ApiModelProperty(value = "采购总数")
    private Integer purchaseTotal;


    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;


    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;


    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;


    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;


    @ApiModelProperty(value = "素材需求")
    private String sourceMaterial;


    @ApiModelProperty(value = "卖点描述")
    private String sampleDescribe;

    @ApiModelProperty(value = "次品样品单")
    private String defectiveSampleChildOrderNo;

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;
}
