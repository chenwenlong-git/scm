package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/3/7 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleSpecialCreateDto extends SampleCreateDto {
    @ApiModelProperty(value = "子单信息")
    @NotEmpty(message = "子单信息不能为空")
    @Valid
    private List<SampleSpecialItemDto> sampleChildItemList;
}
