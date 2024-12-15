package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/1 14:20
 */
@Data
@NoArgsConstructor
public class SampleDeliverNoListDto {
    @ApiModelProperty(value = "样品发货单号列表")
    @Valid
    @NotEmpty(message = "样品发货单号列表不能为空")
    private List<String> sampleDeliverOrderNoList;
}
