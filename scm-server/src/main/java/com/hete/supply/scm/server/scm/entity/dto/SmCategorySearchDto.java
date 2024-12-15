package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/19 15:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SmCategorySearchDto extends ComPageDto {
    @ApiModelProperty(value = "类目名称")
    private String categoryName;
}
