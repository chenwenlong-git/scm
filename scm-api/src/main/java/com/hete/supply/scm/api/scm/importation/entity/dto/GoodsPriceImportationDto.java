package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/6/18 17:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GoodsPriceImportationDto extends BaseImportationRowDto {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "通用渠道")
    private String universalChannelName;

    @ApiModelProperty(value = "渠道名称1")
    private String channelName1;

    @ApiModelProperty(value = "渠道价格1")
    private String channelPrice1;


    @ApiModelProperty(value = "渠道名称2")
    private String channelName2;

    @ApiModelProperty(value = "渠道价格2")
    private String channelPrice2;


    @ApiModelProperty(value = "渠道名称3")
    private String channelName3;

    @ApiModelProperty(value = "渠道价格3")
    private String channelPrice3;


    @ApiModelProperty(value = "渠道名称4")
    private String channelName4;

    @ApiModelProperty(value = "渠道价格4")
    private String channelPrice4;

    @ApiModelProperty(value = "渠道名称5")
    private String channelName5;

    @ApiModelProperty(value = "渠道价格5")
    private String channelPrice5;

    @ApiModelProperty(value = "渠道名称6")
    private String channelName6;

    @ApiModelProperty(value = "渠道价格6")
    private String channelPrice6;

    @ApiModelProperty(value = "渠道名称7")
    private String channelName7;

    @ApiModelProperty(value = "渠道价格7")
    private String channelPrice7;

    @ApiModelProperty(value = "渠道名称8")
    private String channelName8;

    @ApiModelProperty(value = "渠道价格8")
    private String channelPrice8;

    @ApiModelProperty(value = "渠道名称9")
    private String channelName9;

    @ApiModelProperty(value = "渠道价格9")
    private String channelPrice9;

    @ApiModelProperty(value = "渠道名称10")
    private String channelName10;

    @ApiModelProperty(value = "渠道价格10")
    private String channelPrice10;


}
