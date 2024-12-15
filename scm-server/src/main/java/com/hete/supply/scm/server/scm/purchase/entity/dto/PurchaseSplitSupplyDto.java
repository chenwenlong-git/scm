package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/30 21:22
 */
@Data
@NoArgsConstructor
public class PurchaseSplitSupplyDto {
    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @NotNull(message = "补交数不能为空")
    @ApiModelProperty(value = "补交数")
    @Min(value = 1, message = "补交数不能小于0")
    private Integer supplyPurchaseCnt;

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

    @NotNull(message = "期望上架时间不能为空")
    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;
}
