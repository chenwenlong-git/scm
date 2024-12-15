package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/1/23 15:08
 */
@Data
@NoArgsConstructor
public class StockUpItemExportVo {
    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "分类名(一级或二级名称)")
    private String categoryName;

    @ApiModelProperty(value = "备货单状态")
    private StockUpOrderStatus stockUpOrderStatus;

    @ApiModelProperty(value = "备货单状态")
    private String stockUpOrderStatusStr;

    @ApiModelProperty(value = "入库数")
    private Integer warehousingCnt;

    @ApiModelProperty(value = "回货数")
    private Integer returnGoodsCnt;


    @ApiModelProperty(value = "回货时间")
    private LocalDateTime returnGoodsDate;

    @ApiModelProperty(value = "回货时间")
    private String returnGoodsDateStr;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

}
