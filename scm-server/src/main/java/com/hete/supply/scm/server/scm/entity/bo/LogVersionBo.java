package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/20 18:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogVersionBo {
    @NotNull(message = "key不能为空")
    @ApiModelProperty(value = "key")
    private String key;

    @ApiModelProperty(value = "值类型")
    private LogVersionValueType valueType;

    @ApiModelProperty(value = "value")
    private Object value;
}
