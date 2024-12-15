package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingStatus;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/21 09:42
 */
@Data
@NoArgsConstructor
public class DefectHandingSearchVo {
    @ApiModelProperty(value = "次品处理单号")
    private String defectHandlingNo;

    @ApiModelProperty(value = "次品处理状态")
    private DefectHandlingStatus defectHandlingStatus;

    @ApiModelProperty(value = "次品来源单据号：采购单号、加工单号")
    private String defectBizNo;

    @ApiModelProperty(value = "次品类型（比如大货、库内、加工）")
    private DefectHandlingType defectHandlingType;

    @ApiModelProperty(value = "次品方案（次品退供、次品报废、次品换货、次品让步）")
    private DefectHandlingProgramme defectHandlingProgramme;

    @ApiModelProperty(value = "关联单号：采购发货单号、加工次品记录单号")
    private String relatedOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "退货数量")
    private Integer returnCnt;

    @ApiModelProperty(value = "不良原因")
    private String adverseReason;

    @ApiModelProperty(value = "确认人")
    private String confirmUser;

    @ApiModelProperty(value = "确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "创建人名称")
    private String createUser;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "图片")
    private List<String> fileCodeList;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "处理失败原因")
    private String failReason;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;


    @ApiModelProperty(value = "质检数量")
    private Integer qcCnt;


    @ApiModelProperty(value = "合格数")
    private Integer passCnt;


    @ApiModelProperty(value = "不合格数")
    private Integer notPassCnt;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "关联单据号对应仓库名称")
    private String relatedWarehouseName;

    @ApiModelProperty(value = "批次码对应的供应商")
    private String batchCodeSupplierCode;

    @ApiModelProperty(value = "处理sku")
    private String handleSku;

    @ApiModelProperty(value = "处理sku批次码")
    private String handleSkuBatchCode;

    @ApiModelProperty(value = "平台")
    private String platform;
}
