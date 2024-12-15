package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author RockyHuas
 * @date 2022/11/7 10:10
 */
@Data
@NoArgsConstructor
public class GoodsProcessItemVo {

    @ApiModelProperty(value = "明细 ID")
    private Long goodsProcessRelationId;

    @ApiModelProperty(value = "序号")
    private Integer sort;

    @ApiModelProperty(value = "工序 ID")
    private Long processId;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
