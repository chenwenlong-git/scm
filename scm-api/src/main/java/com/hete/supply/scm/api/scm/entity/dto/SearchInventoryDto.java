package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.InventoryStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/8 20:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SearchInventoryDto extends ComPageDto {
    @ApiModelProperty(value = "id")
    private List<Long> supplierInventoryIdList;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

    @ApiModelProperty(value = "库存状态")
    private InventoryStatus inventoryStatus;

    @ApiModelProperty(value = "商品类目id")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "skuList(用于产品名称查询)")
    private List<String> skuList;

    @Size(max = 200, message = "导出勾选最多一次性勾选200条")
    @ApiModelProperty(value = "idList(用于勾选导出)")
    private List<Long> idList;
}
