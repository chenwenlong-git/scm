package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/3/28 14:45
 */
@Data
@NoArgsConstructor
public class SupplierProductComparePageVo {


    @ApiModelProperty(value = "ID")
    private Long plmSkuId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "商品类目")
    private List<PlmCategoryVo> categoryList;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "状态")
    private BindingSupplierProduct bindingSupplierProduct;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "已绑定供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;

    @ApiModelProperty(value = "单件产能")
    private BigDecimal singleCapacity;

}
