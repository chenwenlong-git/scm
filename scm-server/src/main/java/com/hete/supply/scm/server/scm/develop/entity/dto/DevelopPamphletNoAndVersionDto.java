package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopPamphletNoAndVersionDto {

    @ApiModelProperty(value = "版单号")
    @NotNull(message = "版单号不能为空")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;

}
