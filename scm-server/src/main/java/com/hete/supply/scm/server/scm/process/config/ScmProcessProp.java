package com.hete.supply.scm.server.scm.process.config;

import com.hete.supply.scm.server.scm.process.entity.bo.DeliveryWarehouseSkuBo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yanjiawei
 * @Description TODO
 * @Date 2024/8/28 09:50
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.process")
@Data
public class ScmProcessProp {
    /**
     * 创建批次码
     */
    private String createBatchCodeSupplierCode;

    /**
     * 复杂工序配置
     */
    private List<Long> complexProcessIds;

    /**
     * 发货仓库编码与加工成品SKU关系列表
     */
    private List<DeliveryWarehouseSkuBo> deliveryWarehouseSku;
}
