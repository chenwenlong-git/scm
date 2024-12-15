package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleProcessDescDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Data
public class SampleChildEditDetailDto {

    @NotBlank(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "生产信息列表")
    @Valid
    private List<SampleChildOrderInfoDto> sampleChildOrderInfoList;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    private List<SampleRawDto> sampleRawList;

    @ApiModelProperty(value = "样品工序列表")
    @Valid
    private List<SampleProcessDto> sampleProcessList;

    @ApiModelProperty(value = "样品工序描述列表")
    @Valid
    private List<SampleProcessDescDto> sampleProcessDescList;

}
