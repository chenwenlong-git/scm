package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author rockyHuas
 * @date 2023/2/16 22:04
 */
@Data
@NoArgsConstructor
public class SampleChildNoListDto {

    @NotEmpty(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private List<String> sampleChildOrderNoList;
}
