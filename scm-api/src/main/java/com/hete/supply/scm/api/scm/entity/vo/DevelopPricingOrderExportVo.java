package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:15
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderExportVo {

    @ApiModelProperty(value = "核价单")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "处理方式名称")
    private String developSampleMethodName;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;


    @ApiModelProperty(value = "备注")
    private String remarks;


}
