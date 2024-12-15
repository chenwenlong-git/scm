package com.hete.supply.scm.server.scm.supplier.entity.bo;

import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author ChenWenLong
 * @date 2024/8/5 16:25
 */
@Data
@NoArgsConstructor
public class SkuGetSuggestSupplierBo {

    @ApiModelProperty(value = "当前步骤是否直接返回数据")
    private BooleanType isResult;

    @ApiModelProperty(value = "返回的列表信息")
    private List<SkuGetSuggestSupplierListBo> skuGetSuggestSupplierBoList;

    @Data
    public static class SkuGetSuggestSupplierListBo {
        @ApiModelProperty(value = "业务ID")
        private Long businessId;

        @ApiModelProperty(value = "业务ID的数据直接返回数据")
        private BooleanType isIdResult;

        @ApiModelProperty(value = "推荐供应商信息")
        private List<SkuGetSuggestSupplierItemBo> skuGetSuggestSupplierItemBoList;

        @ApiModelProperty(value = "全部绑定供应商信息")
        private List<SkuGetSuggestSupplierItemAllBo> skuGetSuggestSupplierItemAllBoList;

        @Data
        public static class SkuGetSuggestSupplierItemBo {

            @ApiModelProperty(value = "供应商代码")
            private String supplierCode;

            @ApiModelProperty(value = "供应商别称")
            private String supplierAlias;

            @ApiModelProperty(value = "是否默认值推荐")
            private BooleanType isDefault;

            @ApiModelProperty(value = "供应商信息PO")
            private SupplierPo supplierPo;

            @ApiModelProperty(value = "采购子单单号")
            private String purchaseChildOrderNo;

            @ApiModelProperty(value = "采购计划确认时间")
            private LocalDateTime planConfirmTime;

        }

        @Data
        public static class SkuGetSuggestSupplierItemAllBo {

            @ApiModelProperty(value = "供应商代码")
            private String supplierCode;

            @ApiModelProperty(value = "供应商别称")
            private String supplierAlias;

        }
    }


}
