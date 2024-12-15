package com.hete.supply.scm.server.scm.supplier.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ChenWenLong
 * @date 2024/9/24 17:24
 */
@Data
@NoArgsConstructor
public class SupplierDropDownSearchVo {

    @ApiModelProperty(value = "供应商代码")
    private String conditionFieldName;

    @ApiModelProperty(value = "供应商名称")
    private String searchFieldName;

}
