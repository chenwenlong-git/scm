package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/6 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopChildEditDto extends DevelopChildIdAndVersionDto {

    @ApiModelProperty(value = "基础信息")
    @Valid
    private DevelopDetailBaseDto developChildBaseMsgVo;

    @ApiModelProperty(value = "版单信息")
    @Valid
    private DevelopPamphletDetailDto developPamphletMsgVo;

    @ApiModelProperty(value = "样品单信息")
    @Valid
    private List<DevelopSampleOrderDetailDto> developSampleOrderDetailList;


}
