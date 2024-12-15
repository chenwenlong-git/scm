package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/12 11:32
 */
@Data
@NoArgsConstructor
public class PurchaseParentExportVo {
    @ApiModelProperty(value = "需求单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "需求单状态")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;

    @ApiModelProperty(value = "需求单状态")
    private String purchaseParentOrderStatusStr;

    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @ApiModelProperty(value = "需求对象")
    private String skuTypeStr;

    @ApiModelProperty(value = "需求平台")
    private String platform;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "采购总数")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @ApiModelProperty(value = "采购需求类型")
    private String purchaseDemandTypeStr;
}
