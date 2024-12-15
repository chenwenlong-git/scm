package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/3/22 15:16
 */
@Data
@NoArgsConstructor
public class DevelopReviewNoAndVersionDto {

    @NotBlank(message = "审版单号不能为空")
    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;

}
