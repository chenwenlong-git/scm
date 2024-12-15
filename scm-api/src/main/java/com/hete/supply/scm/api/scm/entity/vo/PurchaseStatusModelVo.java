package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/12 11:42
 */
@Data
@NoArgsConstructor
public class PurchaseStatusModelVo {
    private PurchaseOrderStatus purchaseOrderStatus;
}
