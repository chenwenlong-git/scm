package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 质检单实体
 *
 * @author yanjiawei
 * Created on 2023/10/12.
 */
@Data
public class QcOrderVo {

    /**
     * 质检单号id
     */
    private Long qcOrderId;

    /**
     * 质检单号
     */
    private String qcOrderNo;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 收货单号
     */
    private String receiveOrderNo;

    /**
     * 加工单号
     */
    private String processOrderNo;

    /**
     * 质检类型：ALL_CHECK（全检）、SAMPLE_CHECK（抽检）、NOT_CHECK（免检）
     */
    private QcType qcType;

    /**
     * 质检数量
     */
    private Integer qcAmount;

    /**
     * 质检状态：WAIT_HAND_OVER(待交接),TO_BE_QC(待质检),QCING(质检中),TO_BE_AUDIT(待审核),AUDITED(已确认)
     */
    private QcState qcState;

    /**
     * 质检结果:PASSED(合格),FEW_NOT_PASSED(部分不合格),NOT_PASSED(不合格)
     */
    private QcResult qcResult;

    /**
     * 确认交接时间
     */
    private LocalDateTime handOverTime;

    /**
     * 质检任务完成时间
     */
    private LocalDateTime taskFinishTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 交接人
     */
    private String handOverUser;

    /**
     * 质检人
     */
    private String operator;

    /**
     * 质检人
     */
    private String operatorName;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 商品开发类型：NORMAL(常规),LIMITED(limited)
     */
    private String skuDevType;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建人名称
     */
    private String createUsername;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新人名称
     */
    private String updateUsername;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 质检明细实体
     */
    private List<QcOrderDetailVo> qcOrderDetails;

}
