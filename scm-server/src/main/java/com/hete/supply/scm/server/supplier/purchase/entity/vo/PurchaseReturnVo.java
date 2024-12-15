package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:11
 */
@Data
@NoArgsConstructor
public class PurchaseReturnVo {
    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "退货单类型")
    private ReturnType returnType;

    @ApiModelProperty(value = "退货单状态")
    private ReturnOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "预计退货总数")
    private Integer expectedReturnCnt;

    @ApiModelProperty(value = "实际退货总数")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "收货总数")
    private Integer receiptCnt;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "收货人id")
    private String receiptUser;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "创建人id")
    private String createUser;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "退货人id")
    private String returnUser;


    @ApiModelProperty(value = "退货人名称")
    private String returnUsername;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTime;


    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "sku")
    private List<PurchaseSkuCntVo> skuList;

    @ApiModelProperty(value = "供应商产品名称")
    private List<SupplierProductCompareVo> supplierProductCompareVoList;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "总采购数量")
    private Integer purchaseTotal;


    @ApiModelProperty(value = "退货sku明细")
    private List<PurchaseReturnItemVo> purchaseReturnItemList;


    @Data
    private static class SupplierProductCompareVo {

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "供应商产品名称")
        private String supplierProductName;
    }


}
