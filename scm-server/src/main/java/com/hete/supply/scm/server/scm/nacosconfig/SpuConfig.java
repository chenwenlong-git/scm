package com.hete.supply.scm.server.scm.nacosconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * spu相关配置信息
 *
 * @author yanjiawei
 * Created on 2024/4/24.
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "spu")
@Data
public class SpuConfig {
    /**
     * spu提示语 key:spu value:提示语
     */
    private Map<String, String> spuTipMap;
}
