package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/9/20.
 */
@Data
public class UpdateSupSkuCraftAttrDetailDto {
    @NotBlank(message = "缠管不能为空")
    @Length(max = 500, message = "缠管长度字符不能超过500")
    @ApiModelProperty(value = "缠管")
    private String tubeWrapping;

    @NotNull(message = "根数不能为空")
    @Min(value = 0, message = "根数必须大于0")
    @ApiModelProperty(value = "根数")
    private Integer rootsCnt;

    @NotNull(message = "层数不能为空")
    @Min(value = 0, message = "层数必须大于0")
    @ApiModelProperty(value = "层数")
    private Integer layersCnt;

    @NotBlank(message = "特殊处理不能为空")
    @Length(max = 500, message = "特殊处理长度字符不能超过500")
    @ApiModelProperty(value = "特殊处理")
    private String specialHandling;
}
