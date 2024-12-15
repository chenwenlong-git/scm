package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/9/1 13:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseChildOrderSimpleVo {
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商品品类")
    private List<PlmGoodsDetailVo> plmGoodsDetailVoList;
}
