package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */
@Data
@NoArgsConstructor
public class PurchaseCreateDto {
    @ApiModelProperty(value = "spu")
    @Length(max = 100, message = "spu不能超过100个字符")
    private String spu;

    @NotNull(message = "sku类型不能为空")
    @ApiModelProperty(value = "sku类型")
    private SkuType skuType;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    @Length(max = 60, message = "平台字符长度不能超过 60 位")
    private String platform;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码不能超过32个字符")
    private String warehouseCode;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    @Length(max = 32, message = "收货仓库名称不能超过32个字符")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "采购单备注")
    @Length(max = 255, message = "采购单备注长度不能超过255个字符")
    private String orderRemarks;

    @NotNull(message = "采购需求类型不能为空")
    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;


    @ApiModelProperty(value = "采购母单产品明细列表")
    @NotEmpty(message = "采购母单产品明细列表不能为空")
    @Valid
    private List<PurchaseProductDemandItemDto> purchaseProductDemandItemList;
}
