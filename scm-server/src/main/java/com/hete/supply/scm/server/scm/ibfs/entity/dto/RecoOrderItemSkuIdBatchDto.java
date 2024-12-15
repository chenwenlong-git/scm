package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:52
 */
@Data
@NoArgsConstructor
public class RecoOrderItemSkuIdBatchDto {

    @ApiModelProperty(value = "备注")
    @NotBlank(message = "备注不能为空")
    @Length(max = 500, message = "描述字符长度不能超过 500 位")
    private String remarks;

    @ApiModelProperty(value = "对账单详情SKU列表")
    @NotEmpty(message = "对账单详情SKU列表不能为空")
    @Valid
    private List<RecoOrderItemSkuIdAndVersionDto> recoOrderItemSkuIdAndVersionList;

}
