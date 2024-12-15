package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/14 09:20
 */
@NoArgsConstructor
@Data
public class SampleReceiptDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long sampleReceiptOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "收货明细列表")
    @Valid
    @NotEmpty(message = "收货明细列表不能为空")
    private List<SampleReceiptOrderItemDto> sampleReceiptOrderItemList;


}
