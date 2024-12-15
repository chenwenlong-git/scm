package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 16:44
 */
@Data
@NoArgsConstructor
public class SampleSplitDto {

    @NotBlank(message = "样品采购单号不能为空")
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @NotEmpty(message = "样品拆分子项列表不能为空")
    @ApiModelProperty(value = "样品拆分子项列表")
    @Valid
    private List<SampleSplitItem> sampleSplitItemList;

    @Data
    public static class SampleSplitItem {

        @NotBlank(message = "供应商代码不能为空")
        @ApiModelProperty(value = "供应商代码")
        @Length(max = 32, message = "供应商代码不能超过32个字符")
        private String supplierCode;


        @NotNull(message = "采购数不能为空")
        @ApiModelProperty(value = "采购数")
        @Min(value = 1, message = "采购数不能小于0")
        private Integer purchaseCnt;


        @ApiModelProperty(value = "采购预估价")
        private BigDecimal purchasePredictPrice;


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

        @NotNull(message = "业务约定交期不能为空")
        @ApiModelProperty(value = "业务约定交期")
        private LocalDateTime deliverDate;

        @ApiModelProperty(value = "需求描述")
        @Length(max = 255, message = "需求描述不能超过255个字符")
        private String demandDescribe;

        @ApiModelProperty(value = "供应商生产信息")
        @Length(max = 255, message = "供应商生产信息不能超过255个字符")
        private String supplierProduction;
    }

}
