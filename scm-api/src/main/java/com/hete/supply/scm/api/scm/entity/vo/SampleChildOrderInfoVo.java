package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/2 18:15
 */
@Data
@NoArgsConstructor
public class SampleChildOrderInfoVo {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "key")
    private String sampleInfoKey;

    @ApiModelProperty(value = "value")
    private String sampleInfoValue;

    @JsonIgnore
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;
}
