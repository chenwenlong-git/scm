package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/7/31 21:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopChildCreateMqDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "商品类目")
    private String productCategory;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "开发需求")
    private String developDemand;

    @ApiModelProperty(value = "是否加急")
    private String isUrgent;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "开发类型")
    private String developType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;
}
