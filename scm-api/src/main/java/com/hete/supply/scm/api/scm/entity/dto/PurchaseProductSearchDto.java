package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseProductSearchDto extends ComPageDto {
    @ApiModelProperty(value = "采购母单单号")
    private List<String> purchaseParentOrderNoList;

    @ApiModelProperty(value = "采购子单单号批量")
    private List<String> purchaseChildOrderNoList;

    @Size(max = 200, message = "产品名称搜索不能超过200个")
    @ApiModelProperty(value = "产品名称")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "可发货数(可发货订单)[是传1，否传0]")
    private Integer shippableCnt;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "拆分类型（采购类型）")
    private List<PurchaseBizType> purchaseBizTypeList;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "发货单号")
    private List<String> purchaseDeliverOrderNoList;

    @ApiModelProperty(value = "确认人（跟单人）")
    private String confirmUser;

    @ApiModelProperty(value = "确认人（跟单人）")
    private String confirmUsername;

    @ApiModelProperty(value = "订单类型")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDateStart;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDateEnd;

    @ApiModelProperty(value = "业务约定交期(要求发货时间)")
    private LocalDateTime deliverDateStart;

    @ApiModelProperty(value = "业务约定交期(要求发货时间)")
    private LocalDateTime deliverDateEnd;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTimeStart;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTimeEnd;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "采购单状态批量")
    private List<PurchaseOrderStatus> purchaseOrderStatusList;

    @ApiModelProperty(value = "采购原料类型")
    private PurchaseRawBizType purchaseRawBizType;


    @ApiModelProperty(value = "采购原料类型")
    private List<PurchaseRawBizType> purchaseRawBizTypeList;


    @ApiModelProperty(value = "原料是否剩余")
    private BooleanType rawRemainTab;

    @ApiModelProperty(value = "采购列表排序")
    private PurchaseSort purchaseSort;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "商品类目id")
    @Size(max = 10, message = "商品类目搜索不能超过10个")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "平台")
    private List<String> platformList;

    @ApiModelProperty(value = "收货单号")
    private String purchaseReceiptOrderNo;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTimeStart;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTimeEnd;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTimeStart;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTimeEnd;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "质检时间")
    private LocalDateTime qcTimeStart;

    @ApiModelProperty(value = "质检时间")
    private LocalDateTime qcTimeEnd;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTimeStart;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTimeEnd;

    @ApiModelProperty(value = "供应商产品名称批量")
    private List<String> supplierProductNameList;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "是否未交订单")
    private BooleanType hasUnDeliverCnt;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "是否延后（延后传1，提前传-1）")
    private Integer delayDays;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDateStart;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDateEnd;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime commissioningTimeStart;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime commissioningTimeEnd;
}
