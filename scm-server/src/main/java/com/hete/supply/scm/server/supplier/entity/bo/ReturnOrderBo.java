package com.hete.supply.scm.server.supplier.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/27 14:52
 */
@Data
@NoArgsConstructor
public class ReturnOrderBo {
    @NotNull(message = "预计退货总数不能为空")
    @ApiModelProperty(value = "预计退货总数")
    private Integer expectedReturnCnt;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotNull(message = "退货类型不能为空")
    @ApiModelProperty(value = "退货类型")
    private ReturnType returnType;

    @NotBlank(message = "退货单来源单据号不能为空")
    @ApiModelProperty(value = "退货单来源单据号")
    private String returnBizNo;

    @NotEmpty(message = "退货明细不能为空")
    @ApiModelProperty(value = "退货明细")
    @Valid
    private List<ReturnOrderItemBo> purchaseReturnOrderItemBoList;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "操作人")
    @NotBlank(message = "操作人不能为空")
    private String operator;

    @ApiModelProperty(value = "操作人")
    @NotBlank(message = "操作人不能为空")
    private String operatorUsername;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "平台")
    private String platform;

}
