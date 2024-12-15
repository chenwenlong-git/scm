package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/4/10 10:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseProcessSplitDto extends PurchaseParentNoDto {
    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotNull(message = "业务约定交期不能为空")
    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "原料收货仓库编码")
    @Length(max = 32, message = "原料收货仓库编码长度不能超过32个字符")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料收货仓库名称")
    @Length(max = 32, message = "原料收货仓库名称长度不能超过32个字符")
    private String rawWarehouseName;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    private List<RawProductItemDto> rawProductItemList;


    @ApiModelProperty(value = "采购产品明细列表")
    @Valid
    @NotEmpty(message = "采购产品明细列表不能为空")
    private List<PurchaseProductItemDto> purchaseProductItemList;
}
