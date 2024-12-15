package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@ApiModel(value = "原料产品明细")
public class RawProductItemDto {
    @ApiModelProperty(value = "主键id")
    private Long purchaseChildOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @ApiModelProperty(value = "bom单位数")
    @Min(value = 1, message = "bom单位数不能小于0")
    private Integer bomCnt;

    @NotNull(message = "出库数不能为空")
    @ApiModelProperty(value = "出库数")
    @Min(value = 1, message = "出库数不能小于0")
    private Integer deliveryCnt;

    @NotNull(message = "原料提供方不能为空")
    @ApiModelProperty(value = "原料提供方（半成品备货版本不需要接这个参数）")
    private RawSupplier rawSupplier;

    @NotBlank(message = "原料发货方编码不能为空")
    @Length(max = 32, message = "原料收货仓库编码长度不能超过32个字符")
    @ApiModelProperty(value = "原料发货方编码（仓库编码/供应商编码）")
    private String rawWarehouseCode;

    @Length(max = 32, message = "原料收货仓库名称长度不能超过32个字符")
    @ApiModelProperty(value = "原料发货方名称（仓库编码/供应商名称）")
    private String rawWarehouseName;

    @ApiModelProperty(value = "记录sku对应的spu")
    private String spu;

    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "指定库位")
    @Valid
    private List<RawLocationItemDto> rawLocationItemList;

}
