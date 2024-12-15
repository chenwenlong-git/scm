package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/8/18 11:18
 */
@Data
@NoArgsConstructor
public class DevelopChildCancelDto extends DevelopChildIdAndVersionDto {

    @NotBlank(message = "取消原因不能为空")
    @Length(max = 30, message = "取消原因不能超过30个字符")
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

}
