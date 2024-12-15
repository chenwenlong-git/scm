package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/12 17:22
 */
@Data
@NoArgsConstructor
public class DevelopChildSubmitOrderItemDto {

    @ApiModelProperty(value = "需求对象")
    @NotNull(message = "需求对象不能为空")
    private SkuType skuType;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;

    @NotNull(message = "采购需求类型不能为空")
    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码不能超过32个字符")
    private String warehouseCode;

    @NotBlank(message = "收货仓库名称不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    @Length(max = 32, message = "收货仓库名称不能超过32个字符")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @NotBlank(message = "spu不能为空")
    @ApiModelProperty(value = "spu")
    private String spu;

    @NotBlank(message = "开发子单号不能为空")
    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "需求备注")
    @Length(max = 255, message = "需求备注长度不能超过 255 个字符")
    private String orderRemarks;

    @ApiModelProperty("下单列表信息")
    @NotEmpty(message = "下单列表信息不能为空")
    @Valid
    private List<DevelopChildSubmitOrderInfoDto> developChildSubmitOrderInfoList;

    @Data
    public static class DevelopChildSubmitOrderInfoDto {

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotNull(message = "采购数不能为空")
        @ApiModelProperty(value = "采购数")
        @Min(value = 1, message = "采购数不能小于0")
        private Integer purchaseCnt;

    }

}
