package com.hete.supply.scm.server.scm.adjust.config;

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
@ConfigurationProperties(prefix = "scm.adjust")
@Data
public class ScmAdjustProp {
    /**
     * 采购审批人
     */
    private String purchaseApproveUser;

    /**
     * 采购审批人
     */
    private String purchaseApproveUsername;

    /**
     * 白名单
     */
    private String whitelist;
}
