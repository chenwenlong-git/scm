package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/3/28 15:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierProductCompareDto extends ComPageDto {

    @ApiModelProperty(value = "供应商产品名")
    private String supplierProductName;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "SKU批量")
    private List<String> skuList;

    @ApiModelProperty(value = "产品名称")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "状态")
    private BindingSupplierProduct bindingSupplierProduct;

    @ApiModelProperty(value = "状态")
    private List<BindingSupplierProduct> bindingSupplierProductList;

    @ApiModelProperty(value = "供应商产品名批量")
    private List<String> supplierProductNameList;

    @ApiModelProperty(value = "plm的产品ID")
    private List<Long> plmSkuIdList;


}
