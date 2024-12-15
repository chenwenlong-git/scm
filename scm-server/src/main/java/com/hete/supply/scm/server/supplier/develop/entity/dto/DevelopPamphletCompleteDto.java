package com.hete.supply.scm.server.supplier.develop.entity.dto;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPamphletIdAndVersionDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderDetailDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopPamphletCompleteDto extends DevelopPamphletIdAndVersionDto {

    @ApiModelProperty(value = "样品单信息")
    @Valid
    private List<DevelopSampleOrderDetailDto> developSampleOrderDetailList;

}
