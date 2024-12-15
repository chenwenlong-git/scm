package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(value = "质检单实体", description = "质检单实体")
public class ReceiveOrderCreateQcOrderResultBo {
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "质检类型：ALL_CHECK（全检）、SAMPLE_CHECK（抽检）、NOT_CHECK（免检）")
    private QcType qcType;

    @ApiModelProperty(value = "质检数量")
    private Integer qcAmount;

    @ApiModelProperty(value = "质检状态：WAIT_HAND_OVER(待交接),TO_BE_QC(待质检),QCING(质检中),TO_BE_AUDIT(待审核),AUDITED(已确认)")
    private QcState qcState;

    @ApiModelProperty(value = "质检结果:PASSED(合格),FEW_NOT_PASSED(部分不合格),NOT_PASSED(不合格)")
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

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "质检明细实体")
    private List<ReceiveOrderCreateQcOrderDetailResultBo> qcOrderDetailBos;
}