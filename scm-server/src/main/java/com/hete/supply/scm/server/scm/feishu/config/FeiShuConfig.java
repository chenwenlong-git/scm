package com.hete.supply.scm.server.scm.feishu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ChenWenLong
 * @date 2023/12/15 16:01
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "feishu")
@Data
public class FeiShuConfig {
    /**
     * 发审批单默认账号
     */
    private String defaultAccount;

    /**
     * 采购下单数回货数表格sheet
     */
    private String purchaseSheet;

    /**
     * 采购下单数回货数表格table
     */
    private String purchaseTable;


    /**
     * 缺货报表
     */
    private String skuStockOutTable;

    /**
     * 缺货数据
     */
    private String skuStockOutSheet;

    /**
     * 缺货原因
     */
    private String skuStockOutReasonSheet;

    /**
     * 订单+sku缺货原因
     */
    private String skuStockOutOrderReasonSheet;

    /**
     * 月度缺货原因
     */
    private String skuStockOutMonthReasonSheet;

    /**
     * 采购发货数据
     */
    private String purchaseDeliverDataSheet;

    /**
     * 采购发货数据
     */
    private String purchaseDeliverDataTable;

    /**
     * 黑五消息推送
     */
    private String bfMsgRobotToken;


}
