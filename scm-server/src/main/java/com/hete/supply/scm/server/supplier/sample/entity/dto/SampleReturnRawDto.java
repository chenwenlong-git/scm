package com.hete.supply.scm.server.supplier.sample.entity.dto;

import com.hete.supply.scm.server.supplier.entity.dto.RawReturnSampleItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/6 16:00
 */
@Data
@NoArgsConstructor
public class SampleReturnRawDto {
    @NotBlank(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    @NotEmpty(message = "原料产品明细列表不能为空")
    private List<RawReturnSampleItemDto> rawProductItemList;
}
