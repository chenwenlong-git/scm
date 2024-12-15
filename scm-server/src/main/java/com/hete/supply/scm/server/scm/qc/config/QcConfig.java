package com.hete.supply.scm.server.scm.qc.config;

import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/4/29.
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "qc")
@Data
public class QcConfig {
    private List<String> purchaseSupplierCodes;
    private Map<QcOriginProperty, List<String>> qcOriginWarehouseMapping;
}
