package com.hete.supply.scm.server.supplier.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/15 13:53
 */
@Data
@NoArgsConstructor
public class SampleDeliverPrintVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "打印人")
    private String printUsername;

    @ApiModelProperty(value = "样品发货打印详情")
    private List<SampleDeliverPrintItemVo> sampleDeliverPrintItemVoList;

}
