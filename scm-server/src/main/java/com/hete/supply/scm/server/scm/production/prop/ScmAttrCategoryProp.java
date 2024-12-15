package com.hete.supply.scm.server.scm.production.prop;

import com.hete.supply.scm.server.scm.production.entity.bo.AttrCategoryInitBo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/27.
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.attr")
@Data
public class ScmAttrCategoryProp {
    private List<AttrCategoryInitBo> categories;
}
