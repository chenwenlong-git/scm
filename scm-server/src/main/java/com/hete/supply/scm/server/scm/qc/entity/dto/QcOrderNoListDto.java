package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/18 10:55
 */
@Data
@NoArgsConstructor
public class QcOrderNoListDto {
    @NotEmpty(message = "质检单号不能为空")
    @ApiModelProperty(value = "质检单号")
    private List<String> qcOrderNoList;
}