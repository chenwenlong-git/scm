package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.process.enums.ProcessOrderOriginal;
import com.hete.support.api.enums.BooleanType;
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
@ApiModel(value = "加工单编辑参数", description = "加工单编辑参数")
public class ProcessOrderEditDto {

    @ApiModelProperty(value = "加工单 ID")
    @NotNull(message = "加工单 ID 不能为空")
    private Long processOrderId;

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
    @JsonIgnore
    private ProcessOrderOriginal processOrderOriginal;

    @ApiModelProperty("生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty("加工产品明细")
    @Valid
    @NotEmpty
    private List<ProcessOrderItem> processOrderItems;

    @ApiModelProperty("原料产品明细")
    @Valid
    private List<ProcessOrderMaterial> processOrderMaterials;

    @ApiModelProperty("加工工序")
    @Valid
    private List<ProcessOrderProcedure> processOrderProcedures;


    @ApiModelProperty("加工描述")
    @Valid
    private List<ProcessOrderDesc> processOrderDescs;


    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @Data
    @ApiModel(value = "原料产品参数", description = "原料产品参数")
    public static class ProcessOrderMaterial {

        @ApiModelProperty(value = "原料产品明细 id")
        private Long processOrderMaterialId;

        @ApiModelProperty(value = "sku")
        @NotBlank(message = "sku 不能为空")
        @Length(max = 100, message = "sku字符长度不能超过 100 位")
        private String sku;

        @ApiModelProperty(value = "出库数量")
        @NotNull(message = "出库数量不能为空")
        @Positive(message = "出库数量必须为正整数")
        private Integer deliveryNum;

        @ApiModelProperty("版本号")
        private Integer version;

    }

    @Data
    @ApiModel(value = "加工工序参数", description = "加工工序参数")
    public static class ProcessOrderProcedure {

        @ApiModelProperty(value = "加工工序明细 id")
        private Long processOrderProcedureId;

        @ApiModelProperty(value = "工序")
        @NotNull(message = "工序不能为空")
        private Long processId;

        @ApiModelProperty(value = "工序排序")
        @NotNull(message = "工序排序不能为空")
        private Integer sort;

        @ApiModelProperty(value = "人工提成")
        @DecimalMin(value = "0", message = "提成必须是数字")
        private BigDecimal commission;

        @ApiModelProperty("版本号")
        private Integer version;

    }

    @Data
    @ApiModel(value = "加工产品参数", description = "加工产品参数")
    public static class ProcessOrderItem {

        @ApiModelProperty(value = "加工产品明细 id")
        private Long processOrderItemId;

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

        @ApiModelProperty(value = "是否首次创建")
        private BooleanType isFirst;

        @ApiModelProperty("版本号")
        private Integer version;
    }

    @Data
    @ApiModel(value = "加工描述参数", description = "加工描述参数")
    public static class ProcessOrderDesc {

        @ApiModelProperty(value = "加工描述明细 id")
        private Long processOrderDescId;

        @ApiModelProperty(value = "加工描述名称")
        @NotBlank(message = "加工描述名称不能为空")
        @Length(max = 32, message = "加工描述字符长度不能超过 32 位")
        private String processDescName;


        @ApiModelProperty(value = "加工描述值")
        @NotBlank(message = "加工描述值不能为空")
        @Length(max = 32, message = "字符长度不能超过 32 位")
        private String processDescValue;

        @ApiModelProperty("版本号")
        private Integer version;

    }
}
