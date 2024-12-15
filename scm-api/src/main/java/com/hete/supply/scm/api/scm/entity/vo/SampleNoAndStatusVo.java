package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/28 10:16
 */
@Data
@NoArgsConstructor
public class SampleNoAndStatusVo {
    @ApiModelProperty(value = "样品采购母/子单号")
    private String sampleOrderNo;


    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;
}
