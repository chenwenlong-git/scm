package com.hete.supply.scm.server.scm.develop.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/11/19 09:50
 */
@Data
@NoArgsConstructor
public class PamphletRawReceiptOrderCreateBo {

    @ApiModelProperty(value = "出库单号")
    @NotBlank(message = "出库单号不能为空")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "版单号")
    @NotBlank(message = "版单号不能为空")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "原料收货类型")
    @NotNull(message = "原料收货类型不能为空")
    private RawReceiptBizType rawReceiptBizType;

}
