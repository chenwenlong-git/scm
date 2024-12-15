package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yanjiawei
 * Created on 2024/11/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProduceDataAttrImportDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "生产周期")
    private String cycle;

    @ApiModelProperty(value = "单件产能")
    private String singleCapacity;

    @ApiModelProperty(value = "重量")
    private String weight;

    @ApiModelProperty(value = "公差")
    private String tolerance;

    @ApiModelProperty(value = "原料需管理")
    private String rawManage;

    @ApiModelProperty(value = "商品采购价格")
    private String goodsPurchasePrice;

    @ApiModelProperty(value = "属性名称1")
    private String attrName1;

    @ApiModelProperty(value = "属性值1")
    private String attrValue1;

    @ApiModelProperty(value = "属性名称2")
    private String attrName2;

    @ApiModelProperty(value = "属性值2")
    private String attrValue2;

    @ApiModelProperty(value = "属性名称3")
    private String attrName3;

    @ApiModelProperty(value = "属性值3")
    private String attrValue3;

    @ApiModelProperty(value = "属性名称4")
    private String attrName4;

    @ApiModelProperty(value = "属性值4")
    private String attrValue4;

    @ApiModelProperty(value = "属性名称5")
    private String attrName5;

    @ApiModelProperty(value = "属性值5")
    private String attrValue5;

    @ApiModelProperty(value = "属性名称6")
    private String attrName6;

    @ApiModelProperty(value = "属性值6")
    private String attrValue6;

    @ApiModelProperty(value = "属性名称7")
    private String attrName7;

    @ApiModelProperty(value = "属性值7")
    private String attrValue7;

    @ApiModelProperty(value = "属性名称8")
    private String attrName8;

    @ApiModelProperty(value = "属性值8")
    private String attrValue8;

    @ApiModelProperty(value = "属性名称9")
    private String attrName9;

    @ApiModelProperty(value = "属性值9")
    private String attrValue9;

    @ApiModelProperty(value = "属性名称10")
    private String attrName10;

    @ApiModelProperty(value = "属性值10")
    private String attrValue10;

    @ApiModelProperty(value = "属性名称11")
    private String attrName11;

    @ApiModelProperty(value = "属性值11")
    private String attrValue11;

    @ApiModelProperty(value = "属性名称12")
    private String attrName12;

    @ApiModelProperty(value = "属性值12")
    private String attrValue12;

    @ApiModelProperty(value = "属性名称13")
    private String attrName13;

    @ApiModelProperty(value = "属性值13")
    private String attrValue13;

    @ApiModelProperty(value = "属性名称14")
    private String attrName14;

    @ApiModelProperty(value = "属性值14")
    private String attrValue14;

    @ApiModelProperty(value = "属性名称15")
    private String attrName15;

    @ApiModelProperty(value = "属性值15")
    private String attrValue15;

    @ApiModelProperty(value = "属性名称16")
    private String attrName16;

    @ApiModelProperty(value = "属性值16")
    private String attrValue16;

    @ApiModelProperty(value = "属性名称17")
    private String attrName17;

    @ApiModelProperty(value = "属性值17")
    private String attrValue17;

    @ApiModelProperty(value = "属性名称18")
    private String attrName18;

    @ApiModelProperty(value = "属性值18")
    private String attrValue18;

    @ApiModelProperty(value = "属性名称19")
    private String attrName19;

    @ApiModelProperty(value = "属性值19")
    private String attrValue19;

    @ApiModelProperty(value = "属性名称20")
    private String attrName20;

    @ApiModelProperty(value = "属性值20")
    private String attrValue20;
}