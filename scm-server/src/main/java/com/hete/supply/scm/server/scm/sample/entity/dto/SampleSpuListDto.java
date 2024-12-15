package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/13 09:36
 */
@Data
@NoArgsConstructor
public class SampleSpuListDto {
    @ApiModelProperty(value = "spu列表")
    @NotEmpty(message = "spu列表不能为空")
    private List<String> spuList;
}
