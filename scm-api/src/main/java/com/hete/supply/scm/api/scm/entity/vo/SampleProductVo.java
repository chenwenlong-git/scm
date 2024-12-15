package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/1 11:43
 */
@Data
@NoArgsConstructor
public class SampleProductVo {
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;

    @ApiModelProperty(value = "参考图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "需求描述")
    private String demandDescribe;
}
