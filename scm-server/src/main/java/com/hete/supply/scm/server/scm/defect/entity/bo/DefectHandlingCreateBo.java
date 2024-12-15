package com.hete.supply.scm.server.scm.defect.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/21 16:11
 */
@Data
@NoArgsConstructor
public class DefectHandlingCreateBo {
    @NotNull(message = "次品类型不能为空")
    @ApiModelProperty(value = "次品类型（比如大货、库内、加工）")
    private DefectHandlingType defectHandlingType;

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

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "质检数量不能为空")
    @ApiModelProperty(value = "质检数量")
    private Integer qcCnt;

    @NotNull(message = "合格数不能为空")
    @ApiModelProperty(value = "合格数")
    private Integer passCnt;

    @NotNull(message = "不合格数不能为空")
    @ApiModelProperty(value = "不合格数")
    private Integer notPassCnt;

    @NotBlank(message = "不良原因不能为空")
    @ApiModelProperty(value = "不良原因")
    private String adverseReason;

    @ApiModelProperty(value = "确认人")
    private String defectCreateUser;

    @ApiModelProperty(value = "确认人")
    private String defectCreateUsername;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


    @ApiModelProperty(value = "次品处理单号")
    private String defectHandlingNo;

    @ApiModelProperty(value = "次品处理单号")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;
}
