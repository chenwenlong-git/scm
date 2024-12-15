package com.hete.supply.scm.server.scm.qc.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/10 09:32
 */
@Data
@NoArgsConstructor
public class QcSearchVo {
    @ApiModelProperty(value = "id")
    private Long qcOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "入库类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "质检类型")
    private QcType qcType;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "质检状态")
    private QcState qcState;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品类目")
    private String goodsCategory;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "发货单号")
    private String scmBizNo;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "质检总数/交接数")
    private Integer amount;

    @ApiModelProperty(value = "确认交接时间")
    private LocalDateTime handOverTime;

    @ApiModelProperty(value = "质检人")
    private String operator;

    @ApiModelProperty(value = "质检人")
    private String operatorName;

    @ApiModelProperty(value = "质检时间")
    private LocalDateTime taskFinishTime;

    @ApiModelProperty(value = "容器信息")
    private List<QcContainerVo> qcContainerList;

    @ApiModelProperty(value = "待质检数量")
    private Integer waitAmount;

    @ApiModelProperty(value = "正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "次品数")
    private Integer notPassAmount;

    @ApiModelProperty(value = "质检结果")
    private QcResult qcResult;

    @ApiModelProperty(value = "计划上架数量")
    private Integer planAmount;

    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "特采数（让步数）")
    private Integer compromiseCnt;

    @ApiModelProperty(value = "报废数")
    private Integer scrapCnt;

    @ApiModelProperty(value = "换货数")
    private Integer exchangeCnt;

    @ApiModelProperty(value = "上架单号")
    private List<String> onShelvesOrderNoList;

    @ApiModelProperty(value = "退货单号")
    private List<String> returnOrderNoList;

    @ApiModelProperty(value = "收货单号")
    private List<String> receiveOrderNoList;

    @ApiModelProperty(value = "sku数量，用于判断是否展示查看更多")
    private Integer qcDetailSize;

    @ApiModelProperty("质检合格率")
    private BigDecimal qcPassRate;

    @ApiModelProperty(value = "交接明细")
    private List<QcDetailHandItemVo> qcDetailHandItemList;

    @ApiModelProperty(value = "次品信息")
    private List<QcUnPassDetailItemVo> qcUnPassDetailItemList;

    @ApiModelProperty(value = "交接人")
    private String handOverUser;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "质检数量")
    private Integer qcAmount;

    @ApiModelProperty(value = "质检来源(质检类型)")
    private QcOrigin qcOrigin;

    @ApiModelProperty(value = "质检来源属性(质检标识)")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "采购订单号/采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "来源单号")
    private String qcSourceOrderNo;

    @ApiModelProperty(value = "来源类型")
    private QcSourceOrderType qcSourceOrderType;
}
