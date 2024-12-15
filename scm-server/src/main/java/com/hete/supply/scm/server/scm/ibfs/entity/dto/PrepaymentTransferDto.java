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
 * @author weiwenxin
 * @date 2024/5/10 17:11
 */
@Data
@NoArgsConstructor
public class PrepaymentTransferDto {
    @Size(max = 20, message = "同时转交数量不能超过20个")
    @Valid
    @NotEmpty(message = "预付款单号不能为空")
    @ApiModelProperty(value = "预付款单号")
    private List<PrepaymentTransferItemDto> prepaymentTransferItemList;


    @NotBlank(message = "转交人不能为空")
    @ApiModelProperty(value = "转交人")
    private String transferUser;

}
