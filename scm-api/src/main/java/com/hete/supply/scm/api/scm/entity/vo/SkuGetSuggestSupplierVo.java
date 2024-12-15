package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/8/5 16:25
 */
@Data
@NoArgsConstructor
public class SkuGetSuggestSupplierVo {

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "供应商列表信息")
    private List<SkuGetSuggestSupplierItemVo> skuGetSuggestSupplierItemList;

    @Data
    public static class SkuGetSuggestSupplierItemVo {

        @ApiModelProperty(value = "供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "供应商别称")
        private String supplierAlias;

        @ApiModelProperty(value = "是否默认值推荐")
        private BooleanType isDefault;

    }

}
