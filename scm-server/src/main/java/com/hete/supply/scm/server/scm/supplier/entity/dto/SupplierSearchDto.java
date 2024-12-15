package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/9/8 14:25
 */
@Data
@NoArgsConstructor
public class SupplierSearchDto {

    @ApiModelProperty(value = "供应商类型批量")
    private List<SupplierType> supplierTypeList;

}
