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
 * @since 2023-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_material_detail_item")
@ApiModel(value = "ProcessMaterialDetailItemPo对象", description = "加工单原料明细详情表")
public class ProcessMaterialDetailItemPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_material_detail_item_id", type = IdType.ASSIGN_ID)
    private Long processMaterialDetailItemId;

    @ApiModelProperty(value = "关联的加工原料明细表")
    private Long processMaterialDetailId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;


}
