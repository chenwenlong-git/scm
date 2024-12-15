package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.MaterialReceiptType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:07
 */
@Data
public class ProcessMaterialReceiptExportVo {


    /**
     * 出库单号
     */
    private String deliveryNo;

    /**
     * 收货状态
     */
    private ProcessMaterialReceiptStatus processMaterialReceiptStatus;

    /**
     * 所需加工单
     */
    private String processOrderNo;

    /**
     * 返修单号
     */
    private String repairOrderNo;

    /**
     * 原料收货类型
     */
    private MaterialReceiptType materialReceiptType;

    /**
     * 下单人名称
     */
    private String placeOrderUsername;

    /**
     * 下单时间
     */
    private LocalDateTime placeOrderTime;


    /**
     * 送货仓库编码
     */
    private String deliveryWarehouseCode;


    /**
     * 送货仓库名称
     */
    private String deliveryWarehouseName;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 收货人
     */
    private String receiptUsername;


    /**
     * 收货时间
     */
    private LocalDateTime receiptTime;

    /**
     * 所属平台
     */
    private String platform;

    /**
     * 总发货数量
     */
    private Integer totalDeliveryNum;

    /**
     * sku
     */
    private String sku;

    /**
     * 发货数量
     */
    private Integer deliveryNum;

    /**
     * 收货数量
     */
    private Integer receiptNum;

    /**
     * 出库备注
     */
    private String deliveryNote;


}
