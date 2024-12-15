package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/27 11:29
 */
@Data
@NoArgsConstructor
public class DefectCompromiseDto {
    @ApiModelProperty(value = "容器码")
    @NotBlank(message = "容器码不能为空")
    private String containerCode;


    @NotEmpty(message = "次品处理单号不能为空")
    @ApiModelProperty(value = "次品处理单号")
    private List<String> defectHandlingNoList;
}
