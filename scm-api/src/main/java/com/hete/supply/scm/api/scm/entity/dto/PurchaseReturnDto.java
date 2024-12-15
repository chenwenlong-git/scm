package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 09:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseReturnDto extends ComPageDto {
    @ApiModelProperty(value = "退货单类型批量")
    private List<ReturnType> returnTypeList;

    @ApiModelProperty(value = "退货单状态")
    private List<ReturnOrderStatus> returnOrderStatusList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "收货人")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "退货单号批量")
    private List<String> returnOrderNoList;

    @JsonIgnore
    @ApiModelProperty(value = "退货单号(通过sku查询)")
    private List<String> returnOrderNoBySkuList;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTimeStart;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTimeEnd;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购子单单号批量")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "是否导出操作")
    private Boolean isExport;

}
