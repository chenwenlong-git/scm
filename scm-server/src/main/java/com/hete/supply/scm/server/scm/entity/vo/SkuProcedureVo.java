package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * sku工序信息
 *
 * @author yanjiawei
 * @date 2023/07/25 09:38
 */
@Data
@NoArgsConstructor
@ApiModel(value = "sku工序信息", description = "sku工序信息")
public class SkuProcedureVo {
    @ApiModelProperty(value = "sku")
    private String sku;
    @ApiModelProperty(value = "工序id")
    private Long processId;
    @ApiModelProperty(value = "工序代码")
    private String processCode;
    @ApiModelProperty(value = "工序名称")
    private String processName;
    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;
    @ApiModelProperty(value = "人工提成")
    private BigDecimal commission;
    @ApiModelProperty(value = "工序排序")
    private Integer sort;
}