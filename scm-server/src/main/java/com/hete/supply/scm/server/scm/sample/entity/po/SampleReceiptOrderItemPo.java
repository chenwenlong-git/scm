package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 样品收货单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_receipt_order_item")
@ApiModel(value = "SampleReceiptOrderItemPo对象", description = "样品收货单明细")
public class SampleReceiptOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_receipt_order_item_id", type = IdType.ASSIGN_ID)
    private Long sampleReceiptOrderItemId;


    @ApiModelProperty(value = "样品收货单号")
    private String sampleReceiptOrderNo;

    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;


}
