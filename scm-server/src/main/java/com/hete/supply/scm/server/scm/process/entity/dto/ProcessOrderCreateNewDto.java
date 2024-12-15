package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/9/11 09:42
 */
@Data
@NoArgsConstructor
public class ProcessOrderCreateNewDto {
    @ApiModelProperty(value = "加工单类型")
    @NotNull(message = "加工单类型不能为空")
    private ProcessOrderType processOrderType;

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

    @ApiModelProperty(value = "平台名称")
    private String platform;

    @ApiModelProperty(value = "平台编码")
    @NotBlank(message = "平台编码不能为空")
    @Length(max = 60, message = "平台编码字符长度不能超过 60 位")
    private String platCode;

    @ApiModelProperty(value = "业务约定日期")
    @NotNull(message = "业务约定交期不能为空")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "子单备注")
    @Length(max = 500, message = "子单备注字符长度不能超过 500 位")
    private String processOrderNote;

    @ApiModelProperty(value = "出库备注")
    @Length(max = 500, message = "出库备注字符长度不能超过 500 位")
    private String deliveryNote;

    @ApiModelProperty(value = "spu")
    @NotBlank(message = "spu不能为空")
    @Length(max = 32, message = "spu字符长度不能超过 32 位")
    private String spu;

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku 不能为空")
    @Length(max = 100, message = "sku字符长度不能超过 100 位")
    private String sku;

    @ApiModelProperty(value = "加工数量")
    @NotNull(message = "加工数不能为空")
    @Positive(message = "加工数必须为正整数")
    private Integer processNum;


    @ApiModelProperty(value = "原料发货仓库编码")
    @Length(max = 32, message = "原料发货仓库编码字符长度不能超过 32 位")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "原料发货仓库名称")
    @Length(max = 32, message = "原料发货仓库名称字符长度不能超过 32 位")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty("生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "bom信息唯一键")
    private Long bomUniqueId;

    @ApiModelProperty("原料产品明细")
    @Valid
    private List<ProcessOrderMaterialDto> processOrderMaterials;

    @ApiModelProperty("加工工序")
    @Valid
    private List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedures;

    @ApiModelProperty("加工描述")
    @Valid
    private List<ProcessOrderCreateDto.ProcessOrderDesc> processOrderDescs;


}
