package com.hete.supply.scm.server.scm.supplier.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ChenWenLong
 * @date 2023/2/14 11:39
 */
@Data
@NoArgsConstructor
public class SupplierQuickSearchVo {

    @ApiModelProperty(value = "供应商代码")
    private String conditionFieldName;

    @ApiModelProperty(value = "供应商名称")
    private String searchFieldName;

}
