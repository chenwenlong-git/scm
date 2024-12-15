package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/5/30 17:04
 */
@Data
@NoArgsConstructor
public class SkuBatchCodeQuickSearchDto {

    @ApiModelProperty(value = "搜索内容")
    @NotBlank(message = "搜索内容不能为空")
    private String searchContent;

}
