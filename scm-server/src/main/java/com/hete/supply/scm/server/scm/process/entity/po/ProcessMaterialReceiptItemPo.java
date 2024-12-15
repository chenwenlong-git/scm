package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 加工单原料表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_material_receipt_item")
@ApiModel(value = "ProcessMaterialReceiptItemPo对象", description = "加工单原料收货明细表")
public class ProcessMaterialReceiptItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_material_receipt_item_id", type = IdType.ASSIGN_ID)
    private Long processMaterialReceiptItemId;


    @ApiModelProperty(value = "关联的原料收货单")
    private Long processMaterialReceiptId;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku 批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;


    @ApiModelProperty(value = "收货数量")
    private Integer receiptNum;


}
