package com.hete.supply.scm.server.scm.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/4/3 01:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleToastMsgVo {
    @ApiModelProperty(value = "样品工序列表")
    private List<SampleProcessVo> sampleProcessList;

    @ApiModelProperty(value = "原料收货仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料收货仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "样品原料列表")
    private List<SampleRawVo> sampleRawList;
}
