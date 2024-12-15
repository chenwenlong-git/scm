package com.hete.supply.scm.server.supplier.sample.entity.bo;

import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptSimpleVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnSimpleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/22 13:47
 */
@Data
@NoArgsConstructor
public class SampleDetailExtraBo {
    @ApiModelProperty(value = "样品发货信息")
    private List<SampleDeliverSimpleVo> sampleDeliverSimpleList;

    @ApiModelProperty(value = "样品收货信息")
    private List<SampleReceiptSimpleVo> sampleReceiptSimpleList;

    @ApiModelProperty(value = "样品退货信息")
    private List<SampleReturnSimpleVo> sampleReturnSimpleVoList;

    @ApiModelProperty(value = "结算信息")
    private List<PurchaseSettleSimpleVo> purchaseSettleSimpleList;

}
