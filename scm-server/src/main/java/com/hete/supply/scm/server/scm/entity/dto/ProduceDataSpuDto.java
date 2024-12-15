package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 15:10
 */
@Data
@NoArgsConstructor
public class ProduceDataSpuDto {
    @NotBlank(message = "spu不能为空")
    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "主图")
    private List<String> spuFileCodeList;

}
