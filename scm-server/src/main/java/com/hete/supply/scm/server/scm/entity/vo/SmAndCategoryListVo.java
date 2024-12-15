package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/30 01:16
 */
@Data
@NoArgsConstructor
public class SmAndCategoryListVo {
    @ApiModelProperty(value = "辅料sku品类列表")
    private List<SmAndCategoryVo> smAndCategoryVoList;
}
