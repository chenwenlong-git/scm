package com.hete.supply.scm.server.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/30 01:13
 */
@Data
@NoArgsConstructor
public class SmAndCategoryVo {
    @ApiModelProperty(value = "类目代码")
    private String categoryCode;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @JsonIgnore
    @ApiModelProperty(value = "父级类目代码", hidden = true)
    private String parentCategoryCode;

    @ApiModelProperty(value = "子品类列表")
    private List<SmAndCategoryVo> childCategoryList;

    @ApiModelProperty(value = "辅料sku列表")
    private List<SmSkuVo> smSkuList;
}
