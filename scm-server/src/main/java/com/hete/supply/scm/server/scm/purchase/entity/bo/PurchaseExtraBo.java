package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseModifyVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSimpleVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/22 09:55
 */
@Data
@NoArgsConstructor
public class PurchaseExtraBo {
    @ApiModelProperty(value = "发货信息")
    private List<PurchaseDeliverVo> purchaseDeliverList;

    @ApiModelProperty(value = "结算信息")
    private List<PurchaseSettleSimpleVo> purchaseSettleSimpleList;

    @ApiModelProperty(value = "退货信息")
    private List<PurchaseReturnSimpleVo> purchaseReturnSimpleList;

    @ApiModelProperty(value = "变更需求")
    private List<PurchaseModifyVo> purchaseModifyList;
}
