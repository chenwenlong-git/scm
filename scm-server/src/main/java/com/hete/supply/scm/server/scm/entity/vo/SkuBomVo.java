package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 14:51
 */
@Data
@NoArgsConstructor
public class SkuBomVo {

    @ApiModelProperty(value = "id")
    private Long produceDataItemId;

    @ApiModelProperty(value = "bom原料列表")
    private List<SkuBomRawVo> skuBomRawList;

    @Data
    public static class SkuBomRawVo {
        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "sku数量")
        private Integer skuCnt;
    }

    @ApiModelProperty(value = "BOM名称")
    private String bomName;

    @ApiModelProperty(value = "生产信息详情关联供应商")
    private List<SkuBomSupplierVo> skuBomSupplierList;

    @Data
    public static class SkuBomSupplierVo {

        @ApiModelProperty(value = "供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "供应商名称")
        private String supplierName;

    }

}
