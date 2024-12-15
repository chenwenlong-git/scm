package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 加工单工序
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_procedure")
@ApiModel(value = "ProcessOrderProcedurePo对象", description = "加工单工序")
public class ProcessOrderProcedurePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_procedure_id", type = IdType.ASSIGN_ID)
    private Long processOrderProcedureId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;


    @ApiModelProperty(value = "工序排序")
    private Integer sort;

    @ApiModelProperty(value = "工序ID")
    private Long processId;

    @ApiModelProperty(value = "工序代码")
    private String processCode;


    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;


    @ApiModelProperty(value = "人工提成")
    private BigDecimal commission;


}
