package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.process.enums.ProcessTemplateType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 工序模版表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_template")
@ApiModel(value = "ProcessTemplatePo对象", description = "工序模版表")
public class ProcessTemplatePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_template_id", type = IdType.ASSIGN_ID)
    private Long processTemplateId;


    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "类型，CATEGORY：品类，SKU：商品sku")
    private ProcessTemplateType processTemplateType;


    @ApiModelProperty(value = "类型值 id")
    private String typeValue;


    @ApiModelProperty(value = "类型值名称")
    private String typeValueName;

    @ApiModelProperty(value = "详情最后更新时间")
    private LocalDateTime detailsLastUpdatedTime;
}
