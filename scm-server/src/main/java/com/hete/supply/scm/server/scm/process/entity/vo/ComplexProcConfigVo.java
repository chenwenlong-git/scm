package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/3.
 */
@Data
public class ComplexProcConfigVo {
    @ApiModelProperty(value = "复杂工序最大加工数")
    private Integer maxNum;

    @ApiModelProperty(value = "复杂工序配置主键id列表")
    private List<Long> complexProcessIds;
}
