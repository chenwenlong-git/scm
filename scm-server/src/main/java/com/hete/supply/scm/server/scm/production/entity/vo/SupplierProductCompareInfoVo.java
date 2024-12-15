package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/10/14 10:51
 */
@Data
@NoArgsConstructor
public class SupplierProductCompareInfoVo {


    @ApiModelProperty(value = "ID")
    private Long plmSkuId;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "商品类目")
    private List<PlmCategoryVo> categoryList;


    @ApiModelProperty(value = "供应商产品对照列表")
    private List<SupplierProductCompareVo> supplierProductCompareList;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;

    @ApiModelProperty(value = "单件产能")
    private BigDecimal singleCapacity;


}
