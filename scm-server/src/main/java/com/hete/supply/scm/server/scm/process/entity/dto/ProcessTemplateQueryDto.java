package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.server.scm.enums.SortOrder;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "工序模版查询参数", description = "工序模版查询参数")
public class ProcessTemplateQueryDto extends ComPageDto {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "品类 id")
    private Long categoryId;

    @JsonIgnore
    @ApiModelProperty(value = "品类 ids")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "sku")
    private String skuName;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序方式")
    private SortOrder sortOrder;
}
