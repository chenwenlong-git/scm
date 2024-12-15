package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderProcedureVo {

    @ApiModelProperty(value = "加工工序明细 id")
    private Long processOrderProcedureId;


    @ApiModelProperty(value = "工序排序")
    private Integer sort;

    @ApiModelProperty(value = "工序id")
    private Long processId;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "一级工序")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "人工提成")
    private BigDecimal commission;

    @ApiModelProperty("版本号")
    private Integer version;


}
