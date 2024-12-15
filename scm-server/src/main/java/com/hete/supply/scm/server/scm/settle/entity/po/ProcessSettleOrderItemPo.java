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
 * 加工结算单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_settle_order_item")
@ApiModel(value = "ProcessSettleOrderItemPo对象", description = "加工结算单明细")
public class ProcessSettleOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_settle_order_item_id", type = IdType.ASSIGN_ID)
    private Long processSettleOrderItemId;


    @ApiModelProperty(value = "关联结算单ID")
    private Long processSettleOrderId;


    @ApiModelProperty(value = "加工结算单号")
    private String processSettleOrderNo;


    @ApiModelProperty(value = "完成人的用户")
    private String completeUser;


    @ApiModelProperty(value = "完成人的用户名")
    private String completeUsername;


    @ApiModelProperty(value = "加工单数")
    private Integer processNum;


    @ApiModelProperty(value = "加工产品数(正品)")
    private Integer skuNum;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


}
