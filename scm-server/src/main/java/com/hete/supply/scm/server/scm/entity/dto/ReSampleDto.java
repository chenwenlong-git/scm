package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 22:33
 */
@Data
@NoArgsConstructor
public class ReSampleDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long sampleChildOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品改善要求")
    private String sampleImprove;

    @ApiModelProperty(value = "样品改善图")
    private List<String> fileCodeList;
}
