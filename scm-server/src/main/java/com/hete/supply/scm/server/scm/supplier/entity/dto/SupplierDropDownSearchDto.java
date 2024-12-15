package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenWenLong
 * @date 2024/9/24 17:21
 */
@Data
public class SupplierDropDownSearchDto {

    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

    @ApiModelProperty(value = "供应商状态")
    private SupplierStatus supplierStatus;

}
