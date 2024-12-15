package com.hete.supply.scm.server.scm.adjust.entity.bo;

import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/6/18 15:55
 */
@Data
@NoArgsConstructor
public class AdjustApproveBo {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;

    @NotNull(message = "审批类型不能为空")
    @ApiModelProperty(value = "审批类型")
    private ApproveType approveType;

    @NotBlank(message = "审批人不能为空")
    @ApiModelProperty(value = "审批人")
    private String approveUser;

    @NotBlank(message = "审批人不能为空")
    @ApiModelProperty(value = "审批人")
    private String approveUsername;


    @ApiModelProperty(value = "订单调价")
    private List<OrderAdjustDetailItemBo> orderAdjustList;

    @ApiModelProperty(value = "商品调价明细")
    private List<GoodsAdjustDetailItemBo> goodsAdjustList;

    @ApiModelProperty(value = "审批人发起人自选")
    private List<@NotBlank String> nodeApproverUserCodeList;
}
