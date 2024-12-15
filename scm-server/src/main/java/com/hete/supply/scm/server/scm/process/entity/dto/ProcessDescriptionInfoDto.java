package com.hete.supply.scm.server.scm.process.entity.dto;/**
 * 工序描述信息
 *
 * @author yanjiawei
 * Created on 2023/9/9.
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * @date 2023年09月09日 17:42
 */
@Data
@NoArgsConstructor
public class ProcessDescriptionInfoDto {
    @ApiModelProperty(value = "加工描述名称")
    @NotBlank(message = "加工描述名称不能为空")
    @Length(max = 32, message = "加工描述名称字符长度不能超过 32 位")
    private String processDescName;


    @ApiModelProperty(value = "加工描述值")
    @NotBlank(message = "加工描述值不能为空")
    @Length(max = 32, message = "加工描述值字符长度不能超过 32 位")
    private String processDescValue;
}
