package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderNoListDto {

    @NotEmpty(message = "样品单号不能为空")
    @ApiModelProperty(value = "样品单号")
    private List<String> developSampleOrderNoList;

}
