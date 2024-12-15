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
 * 质检单上架单关联表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_on_shelves_order")
@ApiModel(value = "QcOnShelvesOrderPo对象", description = "质检单上架单关联表")
public class QcOnShelvesOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "qc_on_shelves_order_id", type = IdType.ASSIGN_ID)
    private Long qcOnShelvesOrderId;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;


    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNo;


    @ApiModelProperty(value = "计划上架数量")
    private Integer planAmount;

    @ApiModelProperty(value = "上架单生成类型")
    private WmsEnum.OnShelvesOrderCreateType type;


}
