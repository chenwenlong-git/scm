package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author ChenWenLong
 * @date 2023/9/19 13:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopExpectedOnShelvesDate extends DevelopChildIdAndVersionDto {

    @ApiModelProperty(value = "版单信息")
    @Valid
    private DevelopPamphletDetailDto developPamphletMsgVo;

}
