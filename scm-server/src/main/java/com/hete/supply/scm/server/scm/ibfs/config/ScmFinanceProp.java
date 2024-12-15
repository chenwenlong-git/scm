package com.hete.supply.scm.server.scm.ibfs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/6/1 15:05
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.finance")
@Data
public class ScmFinanceProp {
    private String whitelist;

    private String prepaymentLink;

    private String settlementLink;

    private String recoOrderLink;
}
