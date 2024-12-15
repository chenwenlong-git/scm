package com.hete.supply.scm.server.scm.production.prop;

import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.server.scm.production.entity.bo.RiskRangeBo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/9/27.
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.attr.risk")
@Data
public class ScmAttrRiskProp {
    private List<String> suppliers;
    private Map<SkuRisk, RiskRangeBo> rule;
}
