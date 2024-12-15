package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeliveryTypeAttribute;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
public class ProcessMaterialReceiptCreateDto {


    @ApiModelProperty(value = "关联的加工单")
    @NotBlank(message = "关联的加工单不能为空")
    @Length(max = 32, message = "加工单字符长度不能超过 32 位")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "出库单编号")
    @NotBlank(message = "关联的出库单编号不能为空")
    @Length(max = 32, message = "出库单编号字符长度不能超过 32 位")
    private String deliveryNo;

    @ApiModelProperty(value = "出库单类型属性")
    private DeliveryTypeAttribute deliveryTypeAttribute;

    @ApiModelProperty(value = "发货数量")
    @NotNull(message = "发货数量不能为空")
    @Positive(message = "发货数量必须为正整数")
    private Integer deliveryNum;

    @ApiModelProperty(value = "发货人")
    @NotBlank(message = "发货人不能为空")
    private String deliveryUser;

    @ApiModelProperty(value = "发货人")
    @NotBlank(message = "发货人不能为空")
    private String deliveryUsername;

    @ApiModelProperty(value = "发货时间")
    @NotNull(message = "发货时间不能为空")
    private LocalDateTime deliveryTime;

    @ApiModelProperty(value = "出库备注")
    private String deliveryNote;


    @ApiModelProperty(value = "发货仓库编码")
    @NotBlank(message = "发货仓库编码不能为空")
    @Length(max = 32, message = "发货仓库编号字符长度不能超过 32 位")
    private String deliveryWarehouseCode;


    @ApiModelProperty(value = "发货仓库名称")
    @NotBlank(message = "发货仓库名称不能为空")
    @Length(max = 32, message = "发货仓库名称字符长度不能超过 32 位")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "加工原料收货明细")
    @Valid
    private List<MaterialReceiptItem> materialReceiptItems;

    @Data
    public static class MaterialReceiptItem {

        /**
         * sku
         */
        @NotBlank(message = "sku 不能为空")
        @Length(max = 100, message = "sku字符长度不能超过 100 位")
        private String sku;

        /**
         * sku 批次码
         */
        @Length(max = 32, message = "sku批次码字符长度不能超过 32 位")
        private String skuBatchCode;

        /**
         * 出库数量
         */
        @NotNull(message = "出库数不能为空")
        @Positive(message = "出库数必须为正整数")
        private Integer deliveryNum;


    }
}
