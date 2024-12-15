package com.hete.supply.scm.server.scm.ibfs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/6/1 15:05
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.batch-code")
@Data
public class ScmBatchCostPriceProp {
    private List<String> abnormalPriceTipsUsers;
}
