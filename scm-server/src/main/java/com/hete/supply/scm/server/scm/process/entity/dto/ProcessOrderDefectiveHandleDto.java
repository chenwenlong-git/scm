package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DefectiveHandleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DefectiveHandleType;
import com.hete.supply.wms.api.WmsEnum;
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
 * @date: 2023/06/1 14:29
 */
@Data
@ApiModel(value = "次品处理参数", description = "次品处理参数")
public class ProcessOrderDefectiveHandleDto {
    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;

    @ApiModelProperty(value = "次品处理方式")
    @NotNull(message = "次品处理方式不能为空")
    private DefectiveHandleMethod defectiveHandleMethod;

    @ApiModelProperty(value = "次品处理类型")
    private DefectiveHandleType defectiveHandleType;

    @ApiModelProperty(value = "sku批次码")
    @NotBlank(message = "sku批次码不能为空")
    private String skuBatchCode;

    @ApiModelProperty(value = "次品数量")
    @NotNull(message = "次品数量不能为空")
    @Positive(message = "次品数量必须为正整数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "负责人编号")
    private String principalUser;

    @ApiModelProperty(value = "负责人名称")
    private String principalUsername;

    @ApiModelProperty(value = "供应商编号")
    private String supplierUser;

    @ApiModelProperty(value = "供应商名称")
    private String supplierUsername;

    @ApiModelProperty(value = "不良原因")
    @Length(max = 150, message = "不良原因字符长度不能超过 150 位")
    private String badReason;

    @ApiModelProperty("图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码字符长度不能超过 32 位")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
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
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty(value = "业务约定日期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "子单备注")
    @Length(max = 500, message = "子单备注字符长度不能超过 500 位")
    private String processOrderNote;

    @ApiModelProperty("原料产品明细")
    private List<@Valid ProcessOrderMaterial> processOrderMaterials;

    @ApiModelProperty("加工工序")
    private List<@Valid ProcessOrderProcedure> processOrderProcedures;

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
