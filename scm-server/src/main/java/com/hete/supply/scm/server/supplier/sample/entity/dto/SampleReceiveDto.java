package com.hete.supply.scm.server.supplier.sample.entity.dto;

import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildIdAndVersionDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleProcessDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/11 00:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleReceiveDto extends SampleChildIdAndVersionDto {
    @ApiModelProperty(value = "是否接单")
    @NotNull(message = "是否接单不能为空")
    private BooleanType isReceived;

    @ApiModelProperty(value = "拒绝接单原因")
    private String refuseReason;

    @ApiModelProperty(value = "样品工序列表")
    private List<SampleProcessDto> sampleProcessList;

    @ApiModelProperty(value = "样品描述列表")
    private List<SampleProcessDescDto> sampleProcessDescList;
}
