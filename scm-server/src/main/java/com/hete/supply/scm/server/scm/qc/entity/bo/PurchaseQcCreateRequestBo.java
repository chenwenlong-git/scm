package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/4/16.
 */
@Data
@ApiModel(description = "采购创建质检单DTO")
public class PurchaseQcCreateRequestBo {

    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单号", example = "PO123456", required = true)
    private String purchaseChildOrderNo;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "质检来源属性")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "采购单商品信息列表")
    private List<PurchaseQcCreateRequestItemBo> purchaseQcCreateRequestItemBos;
}
