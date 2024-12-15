package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/10/13 15:28
 */
@Data
@NoArgsConstructor
public class QcDetailHandItemDto {
    @NotNull(message = "质检详情id不能为空")
    @ApiModelProperty(value = "质检单详情id")
    private Long qcDetailId;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "正品数不能为空")
    @ApiModelProperty(value = "正品数")
    private Integer passAmount;

    @NotNull(message = "质检总数不能为空")
    @ApiModelProperty(value = "质检总数/交接数")
    private Integer amount;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;
}
