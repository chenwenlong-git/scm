package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderDescVo {

    @ApiModelProperty(value = "加工描述明细 id")
    private Long processOrderDescId;

    @ApiModelProperty(value = "加工描述名称")
    private String processDescName;

    @ApiModelProperty(value = "加工描述值")
    private String processDescValue;

    @ApiModelProperty("版本号")
    private Integer version;
}
