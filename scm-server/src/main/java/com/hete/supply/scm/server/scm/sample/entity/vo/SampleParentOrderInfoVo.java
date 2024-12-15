package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/7 11:26
 */
@Data
@NoArgsConstructor
public class SampleParentOrderInfoVo {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "key")
    private String sampleInfoKey;

    @ApiModelProperty(value = "value")
    private String sampleInfoValue;

    @JsonIgnore
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;
}
