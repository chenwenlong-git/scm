package com.hete.supply.scm.server.scm.qc.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/11 19:48
 */
@Data
@NoArgsConstructor
public class QcUnPassDetailItemVo {
    @ApiModelProperty(value = "次品数")
    private Integer notPassAmount;

    @ApiModelProperty(value = "质检不合格原因")
    private List<String> qcNotPassedReasonList;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "问题图片")
    private List<String> problemFileCodeList;

    @ApiModelProperty(value = "不合格明细关联的明细id")
    private Long relationQcDetailId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;
}
