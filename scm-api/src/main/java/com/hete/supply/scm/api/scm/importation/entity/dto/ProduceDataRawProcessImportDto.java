package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 生产资料原料工序导入
 *
 * @author yanjiawei
 * Created on 2024/11/20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProduceDataRawProcessImportDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty("供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "BOM名称")
    private String bomName;

    @ApiModelProperty(value = "原料SKU1")
    private String rawSku1;

    @ApiModelProperty(value = "单件用量1")
    private String skuCnt1;

    @ApiModelProperty(value = "原料SKU2")
    private String rawSku2;

    @ApiModelProperty(value = "单件用量2")
    private String skuCnt2;

    @ApiModelProperty(value = "原料SKU3")
    private String rawSku3;

    @ApiModelProperty(value = "单件用量3")
    private String skuCnt3;

    @ApiModelProperty(value = "原料SKU4")
    private String rawSku4;

    @ApiModelProperty(value = "单件用量4")
    private String skuCnt4;

    @ApiModelProperty(value = "原料SKU5")
    private String rawSku5;

    @ApiModelProperty(value = "单件用量5")
    private String skuCnt5;

    @ApiModelProperty(value = "原料SKU6")
    private String rawSku6;

    @ApiModelProperty(value = "单件用量6")
    private String skuCnt6;

    @ApiModelProperty(value = "原料SKU7")
    private String rawSku7;

    @ApiModelProperty(value = "单件用量7")
    private String skuCnt7;

    @ApiModelProperty(value = "工序1")
    private String process1;

    @ApiModelProperty(value = "二级工序名1")
    private String processSecond1;

    @ApiModelProperty(value = "工序2")
    private String process2;

    @ApiModelProperty(value = "二级工序名2")
    private String processSecond2;

    @ApiModelProperty(value = "工序3")
    private String process3;

    @ApiModelProperty(value = "二级工序名3")
    private String processSecond3;

    @ApiModelProperty(value = "工序4")
    private String process4;

    @ApiModelProperty(value = "二级工序名4")
    private String processSecond4;

    @ApiModelProperty(value = "工序5")
    private String process5;

    @ApiModelProperty(value = "二级工序名5")
    private String processSecond5;
}
