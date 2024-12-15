package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleType;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
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
 * 开发需求样品单
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_sample_order")
@ApiModel(value = "DevelopSampleOrderPo对象", description = "开发需求样品单")
public class DevelopSampleOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_sample_order_id", type = IdType.ASSIGN_ID)
    private Long developSampleOrderId;


    @ApiModelProperty(value = "核价单编号")
    private String developPricingOrderNo;


    @ApiModelProperty(value = "开发需求母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "状态")
    private DevelopSampleStatus developSampleStatus;


    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;


    @ApiModelProperty(value = "处理人")
    private String handleUser;


    @ApiModelProperty(value = "处理人")
    private String handleUsername;


    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;


    @ApiModelProperty(value = "签收人")
    private String signUser;


    @ApiModelProperty(value = "签收人")
    private String signUsername;


    @ApiModelProperty(value = "签收时间")
    private LocalDateTime signTime;


    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;


    @ApiModelProperty(value = "入仓收货单号")
    private String receiptOrderNo;


    @ApiModelProperty(value = "供应商样品报价")
    private BigDecimal samplePrice;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "供应商大货报价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "工序补充")
    private String processRemarks;

    @ApiModelProperty(value = "上架时间")
    private LocalDateTime shelvesTime;

    @ApiModelProperty(value = "平台")
    private String platform;

    @Override
    public int hashCode() {
        return developSampleOrderNo.hashCode();
    }

    @ApiModelProperty(value = "退样签收人")
    private String returnUser;


    @ApiModelProperty(value = "退样签收人")
    private String returnUsername;


    @ApiModelProperty(value = "退样签收时间")
    private LocalDateTime returnTime;

    @ApiModelProperty(value = "样品单类型")
    private DevelopSampleType developSampleType;


    @ApiModelProperty(value = "寄送人")
    private String sendUser;


    @ApiModelProperty(value = "寄送人")
    private String sendUsername;

    @ApiModelProperty(value = "寄送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "sku批次码样品价格")
    private BigDecimal skuBatchSamplePrice;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "sku批次码大货价格")
    private BigDecimal skuBatchPurchasePrice;

    @ApiModelProperty(value = "退样运单号")
    private String returnTrackingNo;

    @ApiModelProperty(value = "货物走向")
    private DevelopSampleDirection developSampleDirection;


}
