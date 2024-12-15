package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/25 17:29
 */
@Data
@NoArgsConstructor
public class PurchaseModifyVo {
    @ApiModelProperty(value = "降档退货单")
    private String downReturnOrderNo;

    @ApiModelProperty(value = "降档退货单状态")
    private ReceiptOrderStatus downReturnOrderStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发起人")
    private String createUsername;

    @ApiModelProperty(value = "变更内容")
    private List<PurchaseModifyItemVo> purchaseModifyItemList;
}
