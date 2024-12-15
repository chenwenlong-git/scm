package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/8/4 13:58
 */
@Data
@NoArgsConstructor
public class DevelopPamphletDetailDto {

    @ApiModelProperty(value = "版单id")
    @NotNull(message = "版单id不能为空")
    private Long developPamphletOrderId;

    @ApiModelProperty(value = "version")
    @NotNull(message = "版单version不能为空")
    private Integer version;

    @ApiModelProperty(value = "要求打版完成时间")
    @NotNull(message = "要求打版完成时间不能为空")
    private LocalDateTime expectedOnShelvesDate;

}
