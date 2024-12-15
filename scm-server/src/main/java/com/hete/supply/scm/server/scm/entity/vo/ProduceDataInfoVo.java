package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/4/18 16:22
 */
@Data
@NoArgsConstructor
public class ProduceDataInfoVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "原料管理")
    private BooleanType rawManage;

}
