package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/16 09:45
 */
@Data
@NoArgsConstructor
public class PurchaseChildEditDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @NotNull(message = "期望上架时间不能为空")
    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @NotNull(message = "拆分类型不能为空")
    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;

    @ApiModelProperty(value = "采购单备注")
    @Length(max = 255, message = "采购单备注长度不能超过255个字符")
    private String orderRemarks;

    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码长度不能超过32个字符")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    @Length(max = 32, message = "收货仓库名称长度不能超过32个字符")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;
}
