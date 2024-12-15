package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.server.scm.enums.MaterialProductQuality;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/12/6 17:32
 */
@Data
@ApiModel(value = "加工单归还原料参数", description = "加工单归还原料参数")
public class ProcessOrderMaterialBackDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "仓库编码")
    @Length(max = 32, message = "仓库编码字符长度不能超过 32 位")
    private String warehouseCode;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @ApiModelProperty("归还的原料skus")
    @Valid
    private List<MaterialBackSku> materialSkus;

    @ApiModelProperty("产品质量：良品/不良品")
    @NotNull(message = "产品质量不能为空")
    private MaterialProductQuality materialProductQuality;

    @Data
    public static class MaterialBackSku {

        @ApiModelProperty(value = "sku批次码")
        @NotBlank(message = "sku批次码不能为空")
        @Length(max = 32, message = "sku批次码字符长度不能超过 32 位")
        private String skuBatchCode;

        @ApiModelProperty(value = "sku")
        @NotBlank(message = "sku 不能为空")
        @Length(max = 100, message = "sku字符长度不能超过 100 位")
        private String sku;

        @ApiModelProperty(value = "出库数量")
        @NotNull(message = "出库数量不能为空")
        @Positive(message = "出库数量必须为正整数")
        private Integer deliveryNum;
    }
}
