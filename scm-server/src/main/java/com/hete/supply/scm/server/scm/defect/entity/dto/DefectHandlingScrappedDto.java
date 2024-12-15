package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/6/26 16:27
 */
@Data
@NoArgsConstructor
public class DefectHandlingScrappedDto {
    @ApiModelProperty(value = "仓库编码")
    @NotBlank(message = "仓库编码不能为空")
    @Length(max = 32, message = "仓库编号字符长度不能超过 32 位")
    private String warehouseCode;

    @NotEmpty(message = "次品处理单号不能为空")
    @ApiModelProperty(value = "次品处理单号")
    private List<String> defectHandlingNoList;
}
