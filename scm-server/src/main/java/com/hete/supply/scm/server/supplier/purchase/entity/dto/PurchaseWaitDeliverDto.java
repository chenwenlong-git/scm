package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/13 21:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseWaitDeliverDto extends ComPageDto {
    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "采购子单单号")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购发货单号")
    private List<String> purchaseDeliverOrderNoList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

    @NotNull(message = "是否直发不能为空")
    @ApiModelProperty(value = "是否直发(采购发货类型)")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "采购约定交期")
    private LocalDateTime deliverDateStart;

    @ApiModelProperty(value = "采购约定交期")
    private LocalDateTime deliverDateEnd;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "是否生成箱唛")
    private BooleanType hasShippingMark;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "供应商产品名称批量")
    private List<String> supplierProductNameList;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;
}
