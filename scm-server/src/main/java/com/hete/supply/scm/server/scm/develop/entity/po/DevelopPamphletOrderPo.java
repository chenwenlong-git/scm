package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DevelopPamphletOrderStatus;
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
 * 开发版单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_pamphlet_order")
@ApiModel(value = "DevelopPamphletOrderPo对象", description = "开发版单表")
public class DevelopPamphletOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_pamphlet_order_id", type = IdType.ASSIGN_ID)
    private Long developPamphletOrderId;


    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "版单状态")
    private DevelopPamphletOrderStatus developPamphletOrderStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "样品数量")
    private Integer developSampleNum;


    @ApiModelProperty(value = "参考价格")
    private BigDecimal proposedPrice;


    @ApiModelProperty(value = "要求打版完成时间")
    private LocalDateTime expectedOnShelvesDate;


    @ApiModelProperty(value = "需求描述")
    private String demandDesc;


    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;


    @ApiModelProperty(value = "开始打版/拒绝时间")
    private LocalDateTime pamphletDate;

    @ApiModelProperty(value = "完成打版时间")
    private LocalDateTime finishPamphletDate;

    @ApiModelProperty(value = "母单创建人")
    private String parentCreateUser;

    @ApiModelProperty(value = "母单创建人")
    private String parentCreateUsername;

    @ApiModelProperty(value = "母单创建时间")
    private LocalDateTime parentCreateTime;

    @ApiModelProperty(value = "供应商样品报价")
    private BigDecimal samplePrice;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "供应商大货报价")
    private BigDecimal purchasePrice;

}
