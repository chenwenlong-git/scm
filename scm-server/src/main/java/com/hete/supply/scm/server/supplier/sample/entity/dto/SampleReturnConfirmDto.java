package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/18 14:55
 */
@Data
@NoArgsConstructor
public class SampleReturnConfirmDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long sampleReturnOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotEmpty(message = "样品退货单明细列表不能为空")
    @ApiModelProperty(value = "样品退货单明细列表")
    @Valid
    private List<SampleReturnItemDto> sampleReturnItemList;
}
