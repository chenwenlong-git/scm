package com.hete.supply.scm.server.scm.feishu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/10/29 16:11
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.bf")
@Data
public class BfMsgConfig {
    private List<String> qcBadList;
    private List<String> qcGoodList;
    private List<String> receiveBadList;
    private List<String> receiveGoodList;
    private List<String> purchaseBadList;
    private List<String> purchaseGoodList;
    private List<String> badImgList;
    private List<String> goodImgList;


    private Integer batchSize;
}
