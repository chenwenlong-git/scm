package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/25.
 */
@Data
public class DeleteAttributeRiskDto {
    @ApiModelProperty(value = "属性风险信息主键id")
    @NotEmpty(message = "属性风险信息列表不能为空")
    private List<Long> attrRiskIdList;

    //属性风险配置全局锁
    private String attrRiskLockKey = "attr_risk_lock_key";
}
