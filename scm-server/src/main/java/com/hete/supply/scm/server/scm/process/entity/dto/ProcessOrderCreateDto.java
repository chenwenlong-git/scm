package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.process.enums.ProcessOrderOriginal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单创建参数", description = "加工单创建参数")
public class ProcessOrderCreateDto {

    @ApiModelProperty(value = "spu")
    @NotBlank(message = "spu不能为空")
    @Length(max = 32, message = "spu字符长度不能超过 32 位")
    private String spu;

    @ApiModelProperty(value = "平台")
    @NotBlank(message = "平台不能为空")
    @Length(max = 60, message = "平台字符长度不能超过 60 位")
    private String platform;

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

    @ApiModelProperty(value = "业务约定日期")
    @NotNull(message = "业务约定交期不能为空")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "关联销售单号")
    @Length(max = 32, message = "销售单号字符长度不能超过 32 位")
    private String orderNo;

    @ApiModelProperty(value = "客户姓名")
    @Length(max = 32, message = "客户姓名字符长度不能超过 32 位")
    private String customerName;

    @ApiModelProperty(value = "类型")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "子单备注")
    @Length(max = 500, message = "子单备注字符长度不能超过 500 位")
    private String processOrderNote;

    @ApiModelProperty(value = "出库备注")
    @Length(max = 500, message = "出库备注字符长度不能超过 500 位")
    private String deliveryNote;

    @ApiModelProperty(value = "订单来源")
    private ProcessOrderOriginal processOrderOriginal;

    @ApiModelProperty(value = "返工类型关联的加工单")
    @JsonIgnore
    private String parentProcessOrderNo;

    @ApiModelProperty(value = "加工波次 ID")
    @JsonIgnore
    private Long processWaveId;

    @ApiModelProperty("生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty("加工产品明细")
    @Valid
    @Size(min = 1, max = 1, message = "加工产品只能有一个")
    private List<ProcessOrderItem> processOrderItems;


    @ApiModelProperty("原料产品明细")
    private List<@Valid ProcessOrderMaterial> processOrderMaterials;


    @ApiModelProperty("加工工序")
    @Valid
    private List<ProcessOrderProcedure> processOrderProcedures;

    @Valid
    @ApiModelProperty("加工描述")
    private List<ProcessOrderDesc> processOrderDescs;

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
    @ApiModel(value = "加工产品明细参数", description = "加工产品明细参数")
    public static class ProcessOrderItem {

        @ApiModelProperty(value = "sku")
        @NotBlank(message = "sku 不能为空")
        @Length(max = 100, message = "sku字符长度不能超过 100 位")
        private String sku;

        @ApiModelProperty(value = "变体属性")
        @Length(max = 32, message = "变体属性字符长度不能超过 32 位")
        private String variantProperties;

        @ApiModelProperty(value = "加工数量")
        @NotNull(message = "加工数不能为空")
        @Positive(message = "加工数必须为正整数")
        private Integer processNum;

        @ApiModelProperty(value = "采购单价")
        @DecimalMin(value = "0", message = "采购单价不能小于0")
        private BigDecimal purchasePrice;
    }


    @Data
    @ApiModel(value = "加工描述明细参数", description = "加工描述明细参数")
    public static class ProcessOrderDesc {

        @ApiModelProperty(value = "加工描述名称")
        @NotBlank(message = "加工描述名称不能为空")
        @Length(max = 32, message = "加工描述字符长度不能超过 32 位")
        private String processDescName;


        @ApiModelProperty(value = "加工描述值")
        @NotBlank(message = "加工描述值不能为空")
        @Length(max = 32, message = "字符长度不能超过 32 位")
        private String processDescValue;

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

        @ApiModelProperty(value = "原料商品对照关系列表")
        private List<ProcessOrderMaterialCompareDto> materialCompareDtoList;
    }
}
