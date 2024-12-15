package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 14:42
 */
@Data
@NoArgsConstructor
public class SkuBomListVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "原料管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "bom表")
    private List<SkuBomVo> bomList;
}
