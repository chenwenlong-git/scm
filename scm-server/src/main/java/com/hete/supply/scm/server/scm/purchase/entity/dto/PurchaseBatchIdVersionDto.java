package com.hete.supply.scm.server.scm.purchase.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@NoArgsConstructor
public class PurchaseBatchIdVersionDto {


    @NotEmpty(message = "id列表列表不能为空")
    private List<Long> purchaseChildOrderIdList;
}
