package com.hete.supply.scm.server.supplier.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 样品退货单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_return_order_item")
@ApiModel(value = "SampleReturnOrderItemPo对象", description = "样品退货单明细")
public class SampleReturnOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_return_order_item_id", type = IdType.ASSIGN_ID)
    private Long sampleReturnOrderItemId;


    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @ApiModelProperty(value = "收货结果")
    private ReceiptOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;
}
