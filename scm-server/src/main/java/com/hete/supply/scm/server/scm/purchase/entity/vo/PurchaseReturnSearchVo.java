package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.supplier.enums.ReturnRelatedOrderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/5/7 11:10
 */
@Data
@NoArgsConstructor
public class PurchaseReturnSearchVo {

    @ApiModelProperty(value = "主键id")
    private Long purchaseReturnOrderId;

    @ApiModelProperty(value = "采购退货单号")
    private String returnOrderNo;


    @ApiModelProperty(value = "退货单状态")
    private ReturnOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "退货单来源单据号")
    private String returnBizNo;


    @ApiModelProperty(value = "退货信息确认人")
    private String returnUser;


    @ApiModelProperty(value = "退货信息确认人")
    private String returnUsername;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTime;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "预计退货总数")
    private Integer expectedReturnCnt;

    @ApiModelProperty(value = "实际退货总数")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "收货总数")
    private Integer receiptCnt;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "收货人id")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "退货类型")
    private ReturnType returnType;

    @ApiModelProperty(value = "关联单据号")
    private String relatedBizNo;

    @ApiModelProperty(value = "关联单据类型")
    private ReturnRelatedOrderType relatedBizType;

    @ApiModelProperty(value = "关联单据创建时间")
    private LocalDateTime relatedBizTime;

    @ApiModelProperty(value = "退货单备注")
    private String note;

    @ApiModelProperty(value = "退货信息确认人")
    private String returnCreateUser;

    @ApiModelProperty(value = "退货信息确认人")
    private String returnCreateUsername;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;

    @ApiModelProperty(value = "平台")
    private String platform;

}
