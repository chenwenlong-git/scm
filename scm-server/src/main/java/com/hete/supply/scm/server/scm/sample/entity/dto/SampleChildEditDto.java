package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/10 11:05
 */
@NoArgsConstructor
@Data
public class SampleChildEditDto {
    @NotBlank(message = "样品采购单号不能为空")
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @NotEmpty(message = "样品拆分子项列表不能为空")
    @ApiModelProperty(value = "样品拆分子项列表")
    private List<SampleChildOrderDto> sampleSplitItemList;
}
