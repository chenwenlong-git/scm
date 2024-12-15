package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/2/29 14:50
 */
@Data
@ApiModel(description = "获取生产资料信息批量查询实体")
public class ProduceDataSkuListDto {

    @ApiModelProperty(value = "SKU列表", allowableValues = "range[1, 200]")
    @NotEmpty(message = "SKU列表不能为空")
    @Size(max = 200, message = "SKU查询列表最大200条")
    private List<String> skuList;

}
