package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingStatus;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 次品处理
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("defect_handling")
@ApiModel(value = "DefectHandlingPo对象", description = "次品处理")
public class DefectHandlingPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "defect_handling_id", type = IdType.ASSIGN_ID)
    private Long defectHandlingId;


    @ApiModelProperty(value = "次品处理单号")
    private String defectHandlingNo;

    @ApiModelProperty(value = "次品处理状态")
    private DefectHandlingStatus defectHandlingStatus;

    @ApiModelProperty(value = "次品方案（次品退供、次品报废、次品换货、次品让步）")
    private DefectHandlingProgramme defectHandlingProgramme;

    @ApiModelProperty(value = "次品类型（比如大货、库内、加工）")
    private DefectHandlingType defectHandlingType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


    @ApiModelProperty(value = "次品来源单据号：采购单号、加工单号")
    private String defectBizNo;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检明细id")
    private Long bizDetailId;

    @ApiModelProperty(value = "关联单号：采购发货单号、加工次品记录单号")
    private String relatedOrderNo;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "退货数量")
    private Integer returnCnt;


    @ApiModelProperty(value = "质检数量")
    private Integer qcCnt;


    @ApiModelProperty(value = "合格数")
    private Integer passCnt;


    @ApiModelProperty(value = "不合格数")
    private Integer notPassCnt;


    @ApiModelProperty(value = "不良原因")
    private String adverseReason;


    @ApiModelProperty(value = "确认人")
    private String confirmUser;


    @ApiModelProperty(value = "确认人")
    private String confirmUsername;


    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "处理失败原因")
    private String failReason;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "次品处理创建人")
    private String defectCreateUser;

    @ApiModelProperty(value = "次品处理创建人")
    private String defectCreateUsername;


    @ApiModelProperty(value = "处理sku")
    private String handleSku;


    @ApiModelProperty(value = "处理sku批次码")
    private String handleSkuBatchCode;

}
