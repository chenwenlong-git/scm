package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class PurchaseSettleOrderProductVo {

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "单据数量")
    private Integer totalNum;

    @ApiModelProperty(value = "列表明细")
    private List<PurchaseSettleOrderProductDetail> purchaseSettleOrderProductDetailList;

    @Data
    public static class PurchaseSettleOrderProductDetail {

        @ApiModelProperty(value = "单据类型")
        private PurchaseSettleItemType purchaseSettleItemType;

        @ApiModelProperty(value = "采购结算单明细ID")
        private Long purchaseSettleOrderItemId;

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "单据时间")
        private LocalDateTime settleTime;

        @ApiModelProperty(value = "SKU数")
        private Integer skuNum;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "单据状态")
        private String statusName;

        @ApiModelProperty(value = "关联采购单据号")
        private String purchaseChildOrderNo;

    }


}
