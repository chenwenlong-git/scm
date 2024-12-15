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
 * 复合工序关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_composite")
@ApiModel(value = "ProcessCompositePo对象", description = "复合工序关系表")
public class ProcessCompositePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_composite_id", type = IdType.ASSIGN_ID)
    private Long processCompositeId;


    @ApiModelProperty(value = "组合工序ID")
    private Long parentProcessId;


    @ApiModelProperty(value = "组合工序代码")
    private String parentProcessCode;


    @ApiModelProperty(value = "非组合工序ID")
    private Long subProcessId;


    @ApiModelProperty(value = "组合工序代码")
    private String subProcessCode;
}
