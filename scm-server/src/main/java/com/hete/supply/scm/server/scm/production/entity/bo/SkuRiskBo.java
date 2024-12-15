package com.hete.supply.scm.server.scm.production.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/29.
 */
@Data
public class SkuRiskBo {
    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "风险等级")
    private SkuRisk level;
}
