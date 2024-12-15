package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author yanjiawei
 * Created on 2024/4/23.
 */
@Data
public class SkuExistenceRequestDto {
    @ApiModelProperty(value = "SKU列表", notes = "最大条数限制200条！")
    private Set<String> skuList;

    @ApiModelProperty(value = "创建时间开始范围", notes = "UTC时间")
    private LocalDateTime createTimeBegin;

    @ApiModelProperty(value = "创建时间结束范围", notes = "UTC时间")
    private LocalDateTime createTimeEnd;
}
