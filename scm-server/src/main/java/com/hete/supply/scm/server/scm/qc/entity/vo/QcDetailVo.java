package com.hete.supply.scm.server.scm.qc.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/11 16:51
 */
@Data
@NoArgsConstructor
public class QcDetailVo {
    @ApiModelProperty(value = "id")
    private Long qcOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "入库类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "质检类型")
    private QcType qcType;

    @ApiModelProperty(value = "发货单号")
    private String scmBizNo;

    @ApiModelProperty(value = "sku")
    private String skuCode;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "质检总数/交接数")
    private Integer amount;

    @ApiModelProperty(value = "质检人")
    private String operator;

    @ApiModelProperty(value = "质检人")
    private String operatorName;

    @ApiModelProperty(value = "确认交接时间")
    private LocalDateTime handOverTime;

    @ApiModelProperty(value = "质检时间")
    private LocalDateTime taskFinishTime;

    @ApiModelProperty(value = "交接明细")
    private List<QcDetailHandItemVo> qcDetailHandItemList;


    @ApiModelProperty(value = "质检结果")
    private QcResult qcResult;

    @ApiModelProperty(value = "正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "次品信息")
    private List<QcUnPassDetailItemVo> qcUnPassDetailItemList;

    @ApiModelProperty(value = "质检状态")
    private QcState qcState;

    @ApiModelProperty(value = "sku生产信息列表")
    private List<QcDetailProduceDataVo> qcDetailProduceDataList;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "交接人")
    private String handOverUser;

    @ApiModelProperty(value = "质检来源(质检类型)")
    private QcOrigin qcOrigin;

    @ApiModelProperty(value = "质检来源属性(质检标识)")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "SPU提示语")
    private String spuTips;
    @ApiModelProperty(value = "采购订单号/采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "来源单号")
    private String qcSourceOrderNo;

    @ApiModelProperty(value = "来源类型")
    private QcSourceOrderType qcSourceOrderType;

}
