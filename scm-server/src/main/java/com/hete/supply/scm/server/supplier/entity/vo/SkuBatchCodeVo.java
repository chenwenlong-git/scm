package com.hete.supply.scm.server.supplier.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmAttrSkuVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderInfoPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/10 09:31
 */
@Data
@NoArgsConstructor
public class SkuBatchCodeVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "生产信息")
    private List<SampleChildOrderInfoPo> sampleParentOrderInfoPoList;

    @ApiModelProperty(value = "sku变体属性")
    private List<PlmAttrSkuVo> variantSkuList;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;
}
