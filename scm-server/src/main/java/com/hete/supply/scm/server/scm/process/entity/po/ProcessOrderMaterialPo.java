package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.CreateType;
import com.hete.supply.wms.api.WmsEnum;
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
@TableName("process_order_material")
@ApiModel(value = "ProcessOrderMaterialPo对象", description = "加工单原料表")
public class ProcessOrderMaterialPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_material_id", type = IdType.ASSIGN_ID)
    private Long processOrderMaterialId;

    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku 批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "原料仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "库位号/货架号")
    private String shelfCode;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "归还数量")
    private Integer backNum;

    @ApiModelProperty(value = "关联的出库单")
    private String deliveryNo;

    @ApiModelProperty(value = "创建类型")
    private CreateType createType;
}
