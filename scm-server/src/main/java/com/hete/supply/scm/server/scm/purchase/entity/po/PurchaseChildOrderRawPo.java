package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购需求子单原料
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_child_order_raw")
@ApiModel(value = "PurchaseChildOrderRawPo对象", description = "采购需求子单原料")
public class PurchaseChildOrderRawPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_child_order_raw_id", type = IdType.ASSIGN_ID)
    private Long purchaseChildOrderRawId;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;

    @ApiModelProperty(value = "采购原料类型")
    private PurchaseRawBizType purchaseRawBizType;

    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;

    @ApiModelProperty(value = "实际消耗数(确认投产时记录)")
    private Integer actualConsumeCnt;

    @ApiModelProperty(value = "额外消耗数(生成发货单时记录)")
    private Integer extraCnt;

    @ApiModelProperty(value = "额外原料出库")
    private RawExtra rawExtra;

    @ApiModelProperty(value = "分配数量")
    private Integer dispenseCnt;


    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;
}
