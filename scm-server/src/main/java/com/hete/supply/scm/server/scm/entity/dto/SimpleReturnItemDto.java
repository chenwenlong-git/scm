package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/12/3 11:04
 */
@Data
@NoArgsConstructor
public class SimpleReturnItemDto {

    @NotBlank(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @NotNull(message = "退货数不能为空")
    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @NotBlank(message = "样品结果编号不能为空")
    @ApiModelProperty(value = "样品结果编号")
    private String sampleResultNo;
}
