package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseSearchNewDto extends ComPageDto {
    @ApiModelProperty(value = "采购母单单号")
    private List<String> purchaseParentOrderNoList;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "sku")
    private List<String> skuList;

    @ApiModelProperty(value = "是否超期")
    private BooleanType isOverdue;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "是否首单")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "下单时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "下单时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "采购需求单状态")
    private List<PurchaseParentOrderStatus> purchaseParentOrderStatusList;

    @ApiModelProperty(value = "是否存在子单")
    private BooleanType isExistsChild;

    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @ApiModelProperty(value = "是否可拆单")
    private BooleanType isSplit;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @ApiModelProperty(value = "平台")
    private List<String> platformList;
}
