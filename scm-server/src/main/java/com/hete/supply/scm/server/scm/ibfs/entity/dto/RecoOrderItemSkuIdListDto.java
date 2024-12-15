package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/7/2 15:44
 */
@Data
@NoArgsConstructor
public class RecoOrderItemSkuIdListDto {

    @ApiModelProperty(value = "对账单详情列表")
    @NotEmpty(message = "对账单详情列表不能为空")
    @Valid
    private List<RecoOrderItemSkuIdAndVersionDto> recoOrderItemSkuIdAndVersionList;

}
