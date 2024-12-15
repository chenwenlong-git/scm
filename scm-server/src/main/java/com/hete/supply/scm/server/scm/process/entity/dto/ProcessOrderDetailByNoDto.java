package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单详情参数", description = "加工单详情参数")
public class ProcessOrderDetailByNoDto {

    @ApiModelProperty(value = "加工单编号")
    @NotNull(message = "加工单编号不能为空")
    private String processOrderNo;

    @ApiModelProperty(value = "是否需要展示商品类目")
    private BooleanType needSpuCategoryList;
}
