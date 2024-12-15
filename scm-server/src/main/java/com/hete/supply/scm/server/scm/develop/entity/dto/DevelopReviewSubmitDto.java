package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/8/31 14:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopReviewSubmitDto extends DevelopReviewNoDto {

    @NotBlank(message = "提交审版人不能为空")
    @ApiModelProperty(value = "提交审版人")
    private String submitReviewUser;

    @NotBlank(message = "提交审版人不能为空")
    @ApiModelProperty(value = "提交审版人")
    private String submitReviewUsername;

}
