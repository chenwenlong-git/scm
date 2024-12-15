package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_material_detail")
@ApiModel(value = "ProcessMaterialDetailPo对象", description = "加工单原料明细表")
public class ProcessMaterialDetailPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_material_detail_id", type = IdType.ASSIGN_ID)
    private Long processMaterialDetailId;

    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "出库单编号")
    private String deliveryNo;

    @ApiModelProperty(value = "发货仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "发货仓库名称")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "备注")
    private String deliveryNote;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;


    @ApiModelProperty(value = "可加工成品数")
    private Integer availableProductNum;


}
