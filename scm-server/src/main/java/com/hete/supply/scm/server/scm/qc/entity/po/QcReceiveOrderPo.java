package com.hete.supply.scm.server.scm.qc.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 质检单收货单关联表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_receive_order")
@ApiModel(value = "QcReceiveOrderPo对象", description = "质检单收货单关联表")
public class QcReceiveOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "qc_receive_order_id", type = IdType.ASSIGN_ID)
    private Long qcReceiveOrderId;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;


    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "供应链单据号")
    private String scmBizNo;

    @ApiModelProperty(value = "入库类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "商品类目")
    private String goodsCategory;


}
