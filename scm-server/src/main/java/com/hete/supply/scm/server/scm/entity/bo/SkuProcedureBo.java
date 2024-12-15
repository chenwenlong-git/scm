package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * sku工序信息
 *
 * @author yanjiawei
 * @date 2023/07/24 20:05
 */
@Data
@NoArgsConstructor
public class SkuProcedureBo {
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