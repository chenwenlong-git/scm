package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseProductItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/18 09:46
 */
@Data
@NoArgsConstructor
public class ProductPurchaseConfirmItemDto {
    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购产品明细列表")
    @Valid
    @NotEmpty(message = "采购产品明细列表不能为空")
    private List<PurchaseProductItemDto> purchaseProductItemList;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码长度不能超过32个字符")
    private String warehouseCode;

    @NotBlank(message = "收货仓库名称不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    @Length(max = 32, message = "收货仓库名称长度不能超过32个字符")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "采购单备注")
    @Length(max = 255, message = "采购单备注长度不能超过255个字符")
    private String orderRemarks;
}
