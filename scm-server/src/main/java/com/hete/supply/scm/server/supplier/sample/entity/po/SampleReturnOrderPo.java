package com.hete.supply.scm.server.supplier.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 样品退货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_return_order")
@ApiModel(value = "SampleReturnOrderPo对象", description = "样品退货单")
public class SampleReturnOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_return_order_id", type = IdType.ASSIGN_ID)
    private Long sampleReturnOrderId;


    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;


    @ApiModelProperty(value = "退货单状态")
    private ReceiptOrderStatus returnOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;


    @ApiModelProperty(value = "收货人id")
    private String receiptUser;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;
}
