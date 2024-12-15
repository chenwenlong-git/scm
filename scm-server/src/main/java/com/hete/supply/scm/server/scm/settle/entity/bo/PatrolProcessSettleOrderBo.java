package com.hete.supply.scm.server.scm.settle.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderScanPo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/6/6 18:32
 */
@Data
@NoArgsConstructor
public class PatrolProcessSettleOrderBo {

    @ApiModelProperty(value = "异常数据记录")
    private List<ProcessSettleOrderType> processSettleOrderTypeList;

    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;

    @ApiModelProperty(value = "扫码记录列表")
    private List<ProcessOrderScanPo> processOrderScanPoList;

    @ApiModelProperty(value = "结算扫码记录列表")
    private List<ProcessSettleOrderScanPo> processSettleOrderScanPoList;

    @ApiModelProperty(value = "补款单详情列表")
    private List<SupplementOrderPo> supplementOrderPoList;

    @ApiModelProperty(value = "结算补款单详情列表")
    List<ProcessSettleOrderBillPo> supplementBillPoList;

    @ApiModelProperty(value = "扣款单详情列表")
    private List<DeductOrderPo> deductOrderPoList;

    @ApiModelProperty(value = "结算扣单详情列表")
    List<ProcessSettleOrderBillPo> deductBillPoList;


}
