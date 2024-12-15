package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/8/3 16:01
 */
@Data
@NoArgsConstructor
public class DevelopOnShelvesMsgVo {
    @ApiModelProperty(value = "是否上架")
    private BooleanType isOnShelves;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "上架完成时间")
    private LocalDateTime onShelvesCompletionDate;

    @ApiModelProperty(value = "上新时间")
    private LocalDateTime newestCompletionDate;

}
