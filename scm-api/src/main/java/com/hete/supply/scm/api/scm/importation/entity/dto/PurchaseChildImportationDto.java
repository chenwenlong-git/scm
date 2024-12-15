package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:13
 */
@Data
public class PurchaseChildImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "采购母单单号")
        private String purchaseParentOrderNo;

        @ApiModelProperty(value = "采购子单单号")
        private String purchaseChildOrderNo;

        @ApiModelProperty(value = "供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "拆分类型")
        private String purchaseBizType;

        @ApiModelProperty(value = "收货仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "收货仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "收货仓库标签")
        private String warehouseTypes;

        @ApiModelProperty(value = "采购约定交期")
        private LocalDateTime deliverDate;

        @ApiModelProperty(value = "样品单子单号")
        private String sampleChildOrderNo;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "采购数(若有降档需求，则与初始采购数不同)")
        private Integer purchaseCnt;

        @ApiModelProperty(value = "采购价")
        private BigDecimal purchasePrice;

        @ApiModelProperty(value = "优惠类型")
        private String discountType;

        @ApiModelProperty(value = "扣减金额")
        private BigDecimal substractPrice;

        @ApiModelProperty(value = "采购单备注")
        private String orderRemarks;
    }
}
