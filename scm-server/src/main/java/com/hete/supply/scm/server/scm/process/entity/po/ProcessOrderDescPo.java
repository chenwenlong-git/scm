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
 * 加工单加工描述
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_desc")
@ApiModel(value = "ProcessOrderDescPo对象", description = "加工单加工描述")
public class ProcessOrderDescPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_desc_id", type = IdType.ASSIGN_ID)
    private Long processOrderDescId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;


    @ApiModelProperty(value = "关联的加工描述名称")
    private String processDescName;


    @ApiModelProperty(value = "关联的加工描述值")
    private String processDescValue;

}
