package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 15:21
 */
@Data
@NoArgsConstructor
public class SampleCreateDto {
    @NotNull(message = "开发类型不能为空")
    @ApiModelProperty(value = "开发类型")
    private SampleDevType sampleDevType;

    /**
     * 2023-01-06 产品修改需求，该字段记录为 选样结果单号
     */
    @ApiModelProperty(value = "次品样品单（选样结果单号）")
    private String defectiveSampleChildOrderNo;

    @ApiModelProperty(value = "商品类目")
    @NotBlank(message = "商品类目不能为空")
    @Length(max = 32, message = "商品类目长度不能超过32个字符")
    private String categoryName;

    @NotBlank(message = "spu不能为空")
    @ApiModelProperty(value = "spu")
    @Length(max = 100, message = "spu不能超过100个字符")
    private String spu;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    @Length(max = 60, message = "平台字符长度不能超过 60 位")
    private String platform;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码长度不能超过32个字符")
    private String warehouseCode;

    @NotBlank(message = "收货仓库名称不能为空")
    @ApiModelProperty(value = "收货仓库名称")
    @Length(max = 32, message = "收货仓库名称长度不能超过32个字符")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @NotNull(message = "业务约定交期不能为空")
    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @NotNull(message = "是否首单不能为空")
    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;

    @NotNull(message = "是否加急不能为空")
    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;

    @ApiModelProperty(value = "素材需求")
    @Length(max = 32, message = "素材需求长度不能超过32个字符")
    private String sourceMaterial;

    @ApiModelProperty(value = "卖点描述")
    @Length(max = 255, message = "卖点描述长度不能超过255个字符")
    private String sampleDescribe;

    @ApiModelProperty(value = "生产信息列表")
    @Valid
    private List<SampleParentOrderInfoDto> sampleParentOrderInfoList;

    @ApiModelProperty(value = "参考图片")
    private List<String> fileCodeList;

    @NotNull(message = "是否打样不能为空")
    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "原料收货仓库编码")
    @Length(max = 32, message = "原料收货仓库编码长度不能超过32个字符")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料收货仓库名称")
    @Length(max = 32, message = "原料收货仓库名称长度不能超过32个字符")
    private String rawWarehouseName;


}
