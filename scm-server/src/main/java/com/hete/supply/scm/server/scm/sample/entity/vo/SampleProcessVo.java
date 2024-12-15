package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 样品子单工序
 *
 * @author weiwenxin
 * @date 2023/3/31 09:38
 */
@Data
@NoArgsConstructor
public class SampleProcessVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderProcessId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序id")
    private Long processId;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty("工序")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;
}
