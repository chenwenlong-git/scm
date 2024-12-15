package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/26 16:27
 */
@Data
@NoArgsConstructor
public class DefectHandlingNoListDto {
    @NotEmpty(message = "次品处理单号不能为空")
    @ApiModelProperty(value = "次品处理单号")
    private List<DefectHandlingNoDto> defectHandlingReturnInsideItemList;
}
