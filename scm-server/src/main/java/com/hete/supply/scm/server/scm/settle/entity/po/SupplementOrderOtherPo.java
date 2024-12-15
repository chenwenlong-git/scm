package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 补款单其他单据明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplement_order_other")
@ApiModel(value = "SupplementOrderOtherPo对象", description = "补款单其他单据明细")
public class SupplementOrderOtherPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplement_order_other_id", type = IdType.ASSIGN_ID)
    private Long supplementOrderOtherId;


    @ApiModelProperty(value = "补款单ID")
    private Long supplementOrderId;

    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;


    @ApiModelProperty(value = "补款备注")
    private String supplementRemarks;


}
