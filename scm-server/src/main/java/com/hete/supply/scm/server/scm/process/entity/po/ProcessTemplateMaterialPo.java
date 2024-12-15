package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.enums.MaterialSkuType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工序模版原料表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-04-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_template_material")
@ApiModel(value = "ProcessTemplateMaterialPo对象", description = "工序模版原料表")
public class ProcessTemplateMaterialPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_template_material_id", type = IdType.ASSIGN_ID)
    private Long processTemplateMaterialId;

    @ApiModelProperty(value = "工序模版 ID")
    private Long processTemplateId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "原料SKU所属类型")
    private MaterialSkuType materialSkuType;
}
