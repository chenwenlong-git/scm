package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/21 16:39
 */
@Data
@NoArgsConstructor
public class RecoOrderTransferDto {
    @Size(max = 20, message = "同时转交数量不能超过20个")
    @Valid
    @NotEmpty(message = "对账单号不能为空")
    @ApiModelProperty(value = "对账单号")
    private List<RecoOrderTransferItemDto> recoOrderTransferItemList;


    @NotBlank(message = "转交人不能为空")
    @ApiModelProperty(value = "转交人")
    private String transferUser;

}
