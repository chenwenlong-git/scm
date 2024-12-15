package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2023/11/14.
 */
@Data
public class QcDetailExportBo {

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "质检类型")
    private QcType qcType;


    @ApiModelProperty(value = "质检数量")
    private Integer qcAmount;


    @ApiModelProperty(value = "质检状态")
    private QcState qcState;


    @ApiModelProperty(value = "质检结果")
    private QcResult qcResult;


    @ApiModelProperty(value = "确认交接时间")
    private LocalDateTime handOverTime;


    @ApiModelProperty(value = "质检任务完成时间")
    private LocalDateTime taskFinishTime;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;


    @ApiModelProperty(value = "交接人")
    private String handOverUser;


    @ApiModelProperty(value = "质检人")
    private String operator;


    @ApiModelProperty(value = "质检人")
    private String operatorName;


    @ApiModelProperty(value = "审核人")
    private String auditor;


    @ApiModelProperty(value = "商品开发类型：NORMAL(常规),LIMITED(limited)")
    private String skuDevType;


    @ApiModelProperty(value = "质检单详情id")
    @TableId(value = "qc_detail_id", type = IdType.ASSIGN_ID)
    private Long qcDetailId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;


    @ApiModelProperty(value = "批次码")
    private String batchCode;


    @ApiModelProperty(value = "赫特spu")
    private String spu;


    @ApiModelProperty(value = "赫特sku")
    private String skuCode;


    @ApiModelProperty(value = "质检总数")
    private Integer amount;


    @ApiModelProperty(value = "待质检数量")
    private Integer waitAmount;


    @ApiModelProperty(value = "通过数量")
    private Integer passAmount;


    @ApiModelProperty(value = "不通过数量")
    private Integer notPassAmount;


    @ApiModelProperty(value = "质检不合格原因")
    private String qcNotPassedReason;


    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "图片id,多个用英文逗号分隔")
    private String pictureIds;


    @ApiModelProperty(value = "sku重量，克")
    private BigDecimal weight;


    @ApiModelProperty(value = "不合格明细关联的明细id")
    private Long relationQcDetailId;

    @ApiModelProperty(value = "发货单号")
    private String scmBizNo;

    @ApiModelProperty(value = "质检来源")
    private QcOrigin qcOrigin;

    @ApiModelProperty(value = "质检来源属性")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "来源单号")
    private String qcSourceOrderNo;

    @ApiModelProperty(value = "来源类型")
    private QcSourceOrderType qcSourceOrderType;

}
