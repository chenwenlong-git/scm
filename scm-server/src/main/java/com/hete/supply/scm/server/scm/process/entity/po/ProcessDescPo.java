package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 加工描述表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_desc")
@ApiModel(value = "ProcessDescPo对象", description = "加工描述表")
public class ProcessDescPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_desc_id", type = IdType.ASSIGN_ID)
    private Long processDescId;


    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "一级工序")
    private ProcessFirst processFirst;


    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValues;


}
