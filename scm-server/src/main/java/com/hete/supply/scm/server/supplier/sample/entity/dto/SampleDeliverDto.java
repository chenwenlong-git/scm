package com.hete.supply.scm.server.supplier.sample.entity.dto;

import com.hete.supply.scm.server.scm.sample.entity.dto.SampleProcessDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/5 01:04
 */
@Data
@NoArgsConstructor
public class SampleDeliverDto {
    @ApiModelProperty(value = "发货单子项")
    @NotEmpty(message = "发货单子项不能为空")
    @Valid
    @Size(max = 1, message = "只能选择一张样品采购子单创建样品发货单")
    private List<SampleDeliverItemDto> sampleDeliverItemList;

    @ApiModelProperty(value = "样品工序列表")
    @Valid
    private List<SampleProcessDto> sampleProcessList;

    @ApiModelProperty(value = "样品工序描述列表")
    @Valid
    private List<SampleProcessDescDto> sampleProcessDescList;

}
