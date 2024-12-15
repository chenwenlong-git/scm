package com.hete.supply.scm.server.scm.settle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 结算补扣款配置信息
 *
 * @author ChenWenLong
 * @date 2024/8/29 15:37
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "settle")
@Data
public class SettleConfig {
    /**
     * 价格确认人
     */
    private String confirmUser;

    /**
     * 审核人
     */
    private String examineUser;


    /**
     * 供应商
     */
    private String supplierName;
}
