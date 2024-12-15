package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/12 11:32
 */
@Data
@NoArgsConstructor
public class PurchaseParentSkuExportVo {
    @ApiModelProperty(value = "需求单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "需求单状态")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;

    @ApiModelProperty(value = "需求单状态")
    private String purchaseParentOrderStatusStr;

    @ApiModelProperty(value = "需求平台")
    private String platform;

    @ApiModelProperty("商品sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "下单数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "sku可拆单数")
    private Integer canSplitCnt;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;

    @ApiModelProperty(value = "入库数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @ApiModelProperty(value = "采购需求类型")
    private String purchaseDemandTypeStr;

}
