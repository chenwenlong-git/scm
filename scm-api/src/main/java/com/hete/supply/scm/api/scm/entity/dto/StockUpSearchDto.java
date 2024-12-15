package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 19:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockUpSearchDto extends ComPageDto {
    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;

    @ApiModelProperty(value = "备货单号")
    private List<String> stockUpOrderNoList;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "sku批量查询")
    private List<String> skuList;

    @ApiModelProperty(value = "备货单状态")
    private List<StockUpOrderStatus> stockUpOrderStatusList;

    @ApiModelProperty(value = "备货单状态，用于供应商系统过滤(前端不传该字段)")
    private List<StockUpOrderStatus> notInStockUpOrderStatusList;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "供应商产品名称")
    private List<SupplierPdcNameItemDto> supplierPdcNameItemList;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;
}
