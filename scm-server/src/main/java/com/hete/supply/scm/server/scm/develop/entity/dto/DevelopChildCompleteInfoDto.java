package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/20 10:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopChildCompleteInfoDto extends DevelopChildIdAndVersionDto {

    @ApiModelProperty(value = "基础信息")
    @Valid
    private DevelopDetailBaseDto developChildBaseMsgVo;

    @ApiModelProperty(value = "版单信息")
    private DevelopPamphletDetailDto developPamphletMsgVo;

    @ApiModelProperty(value = "样品单信息")
    @Valid
    private List<DevelopSampleOrderDetailDto> developSampleOrderDetailList;

    @ApiModelProperty(value = "齐备信息")
    @NotNull(message = "齐备信息不能为空")
    @Valid
    private List<DevelopSampleCompleteInfoDto> developSampleCompleteInfoList;

}
