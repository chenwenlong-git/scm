package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/1/13 16:35
 */
@Data
@NoArgsConstructor
public class SmSkuListDto {
    @ApiModelProperty("辅料sku列表")
    private List<String> smSkuList;
}
