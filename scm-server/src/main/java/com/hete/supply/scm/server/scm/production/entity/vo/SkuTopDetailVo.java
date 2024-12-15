package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.plm.api.goods.enums.SkuDevType;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 17:05
 */
@Data
public class SkuTopDetailVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "商品图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private SkuDevType skuDevType;

    @ApiModelProperty(value = "供应商产品对照列表")
    private List<SupplierProductCompareVo> supplierProductCompareList;

}
