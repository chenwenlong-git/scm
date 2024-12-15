package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SampleReceiptOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 样品收货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_receipt_order")
@ApiModel(value = "SampleReceiptOrderPo对象", description = "样品收货单")
public class SampleReceiptOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_receipt_order_id", type = IdType.ASSIGN_ID)
    private Long sampleReceiptOrderId;


    @ApiModelProperty(value = "样品收货单号")
    private String sampleReceiptOrderNo;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;


    @ApiModelProperty(value = "总收货数")
    private Integer totalReceipt;


    @ApiModelProperty(value = "样品收货单状态")
    private SampleReceiptOrderStatus receiptOrderStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "收货人id")
    private String receiptUser;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

}
