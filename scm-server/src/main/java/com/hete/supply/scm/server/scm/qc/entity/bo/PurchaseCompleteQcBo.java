package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 采购订单批量完成质检实体
 *
 * @author yanjiawei
 * Created on 2024/4/15.
 */
@Data
public class PurchaseCompleteQcBo {
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "质检来源属性")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "采购单商品信息列表")
    private List<PurchaseCompleteQcItemBo> purchaseCompleteQcItemBos;
}
