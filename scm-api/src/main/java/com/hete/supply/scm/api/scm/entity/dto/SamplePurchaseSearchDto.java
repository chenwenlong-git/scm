package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 17:12
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SamplePurchaseSearchDto extends ComPageDto {
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "发货单号")
    private String sampleDeliverOrderNo;


    @ApiModelProperty(value = "收货单号")
    private String sampleReceiptOrderNo;


    @ApiModelProperty(value = "质检单号")
    private String sampleQcOrderNo;


    @ApiModelProperty(value = "入库单号")
    private String sampleWarehousingOrderNo;

    @ApiModelProperty(value = "结算单号")
    private String sampleSettleOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private List<SampleOrderStatus> sampleOrderStatusList;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @ApiModelProperty(value = "审核人")
    private String approveUser;

    @ApiModelProperty(value = "接单人")
    private String receiveUser;

    @ApiModelProperty(value = "发货人")
    private String deliverUser;

    @ApiModelProperty(value = "收货人")
    private String receiptUser;

    @ApiModelProperty(value = "选样人")
    private String sampleUser;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @ApiModelProperty(value = "审核人")
    private String approveUsername;

    @ApiModelProperty(value = "接单人")
    private String receiveUsername;

    @ApiModelProperty(value = "发货人")
    private String deliverUsername;

    @ApiModelProperty(value = "收货人")
    private String receiptUsername;

    @ApiModelProperty(value = "选样人")
    private String sampleUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTimeStart;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTimeEnd;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTimeStart;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTimeEnd;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTimeStart;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTimeEnd;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTimeStart;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTimeEnd;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDateStart;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDateEnd;

    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;

    @ApiModelProperty(value = "spu批量")
    private List<String> spuList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "平台批量")
    private List<String> platformList;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称批量")
    private List<String> supplierNameList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "下发打版时间")
    private LocalDateTime typesetTimeStart;

    @ApiModelProperty(value = "下发打版时间")
    private LocalDateTime typesetTimeEnd;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "样品采购子单号批量")
    private List<String> sampleChildOrderNoList;

    @ApiModelProperty(value = "sku")
    private String sku;


}
