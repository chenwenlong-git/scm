package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RecoOrderChangeHandleDto extends RecoOrderNoAndVersionDto {

    @ApiModelProperty(value = "处理人")
    @NotBlank(message = "处理人不能为空")
    private String handleUser;

    @ApiModelProperty(value = "处理人的名称")
    @NotBlank(message = "处理人的名称不能为空")
    private String handleUsername;

}
