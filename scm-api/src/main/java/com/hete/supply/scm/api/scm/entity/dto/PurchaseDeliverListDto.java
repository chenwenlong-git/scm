package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 11:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseDeliverListDto extends ComPageDto {
    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "发货人")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货单状态")
    private List<DeliverOrderStatus> deliverOrderStatusList;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;


    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "采购发货单号批量")
    private List<String> purchaseDeliverOrderNoList;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "供应商产品名称批量")
    private List<String> supplierProductNameList;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "入库时间开始")
    private LocalDateTime warehousingTimeStart;

    @ApiModelProperty(value = "入库时间结束")
    private LocalDateTime warehousingTimeEnd;

    @ApiModelProperty(value = "支付完成时间开始")
    private LocalDateTime payTimeStart;

    @ApiModelProperty(value = "支付完成时间结束")
    private LocalDateTime payTimeEnd;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购子单单号批量")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "跟单确认人")
    private String confirmUser;

}
