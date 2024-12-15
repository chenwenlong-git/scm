package com.hete.supply.scm.server.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.server.supplier.enums.CategoryLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/19 15:32
 */
@Data
@NoArgsConstructor
public class SmCategorySearchVo {
    @ApiModelProperty(value = "id")
    private Long smCategoryId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "类目代码")
    private String categoryCode;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "类目级别")
    private CategoryLevel categoryLevel;

    @JsonIgnore
    @ApiModelProperty(value = "父级类目代码", hidden = true)
    private String parentCategoryCode;

    @ApiModelProperty(value = "子品类列表")
    private List<SmCategorySearchVo> childCategoryList;
}
