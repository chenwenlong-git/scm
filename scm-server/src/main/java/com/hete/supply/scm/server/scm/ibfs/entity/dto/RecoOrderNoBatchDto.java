package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:52
 */
@Data
@NoArgsConstructor
public class RecoOrderNoBatchDto {

    @ApiModelProperty(value = "对账单信息")
    @NotEmpty(message = "对账单信息列表不能为空")
    @Valid
    private List<RecoOrderNoAndVersionDto> recoOrderNoAndVersionList;

}
