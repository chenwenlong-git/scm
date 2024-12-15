package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/13 09:33
 */
@Data
@NoArgsConstructor
public class SampleMsgChildVo {
    @JsonIgnore
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;
}
