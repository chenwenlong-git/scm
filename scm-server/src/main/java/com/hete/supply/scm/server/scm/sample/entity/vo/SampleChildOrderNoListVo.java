package com.hete.supply.scm.server.scm.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/9 10:14
 */
@Data
@NoArgsConstructor
public class SampleChildOrderNoListVo {
    @ApiModelProperty(value = "样品采购子单号列表")
    private List<String> sampleChildOrderNoList;
}
