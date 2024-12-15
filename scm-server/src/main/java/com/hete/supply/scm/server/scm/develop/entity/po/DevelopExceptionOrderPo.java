package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.develop.enums.DevelopChildExceptionalType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 开发异常处理表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_exception_order")
@ApiModel(value = "DevelopExceptionOrderPo对象", description = "开发异常处理表")
public class DevelopExceptionOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_exception_order_id", type = IdType.ASSIGN_ID)
    private Long developExceptionOrderId;


    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;


    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "异常处理方式")
    private DevelopChildExceptionalType developChildExceptionalType;


    @ApiModelProperty(value = "取消原因")
    private String cancelReason;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "新版单号")
    private String newDevelopPamphletOrderNo;


    @ApiModelProperty(value = "需求描述")
    private String demandDesc;


}
