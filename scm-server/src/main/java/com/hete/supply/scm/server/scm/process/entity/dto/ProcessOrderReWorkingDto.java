package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2023/03/30 14:29
 */
@Data
@ApiModel(value = "加工单次品返工参数", description = "加工单次品返工参数")
public class ProcessOrderReWorkingDto {

    @ApiModelProperty(value = "仓库编码")
    @NotBlank(message = "仓库不能为空")
    @Length(max = 32, message = "仓库编码字符长度不能超过 32 位")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    @NotBlank(message = "仓库不能为空")
    @Length(max = 32, message = "仓库名称字符长度不能超过 32 位")
    private String warehouseName;

    @ApiModelProperty(value = "仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "原料发货仓库编码")
    @Length(max = 32, message = "原料发货仓库编码字符长度不能超过 32 位")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "原料发货仓库名称")
    @Length(max = 32, message = "原料发货仓库名称字符长度不能超过 32 位")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "产品质量")
    @NotNull(message = "产品质量不能为空")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty(value = "业务约定日期")
    @NotNull(message = "业务约定交期不能为空")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "子单备注")
    @Length(max = 500, message = "子单备注字符长度不能超过 500 位")
    private String processOrderNote;

    @ApiModelProperty(value = "返工类型关联的加工单")
    @NotBlank
    private String parentProcessOrderNo;

    @ApiModelProperty(value = "返工数量")
    @NotNull(message = "返工数量不能为空")
    @Positive(message = "返工数量必须为正整数")
    private Integer processNum;


    @ApiModelProperty("原料产品明细")
    @Valid
    private List<ProcessOrderMaterial> processOrderMaterials;


    @ApiModelProperty("加工工序")
    @Valid
    private List<ProcessOrderProcedure> processOrderProcedures;

    @Data
    public static class ProcessOrderProcedure {

        @ApiModelProperty(value = "工序")
        @NotNull(message = "工序不能为空")
        private Long processId;

        @ApiModelProperty(value = "工序排序")
        @NotNull(message = "工序排序不能为空")
        private Integer sort;

        @ApiModelProperty(value = "人工提成")
        @DecimalMin(value = "0", message = "提成必须是数字")
        private BigDecimal commission;

    }


    @Data
    @ApiModel(value = "原料产品明细参数", description = "原料产品明细参数")
    public static class ProcessOrderMaterial {

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
