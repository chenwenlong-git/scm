package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 质检单明细信息
 *
 * @author yanjiawei
 * Created on 2023/10/12.
 */
@Data
public class QcOrderDetailVo {
    /**
     * 质检单详情id
     */
    private Long qcDetailId;

    /**
     * 质检单号
     */
    private String qcOrderNo;

    /**
     * 容器编码
     */
    private String containerCode;

    /**
     * 批次码
     */
    private String batchCode;

    /**
     * 赫特spu
     */
    private String spu;

    /**
     * 赫特sku
     */
    private String skuCode;

    /**
     * (应)质检总数量
     */
    private Integer amount;

    /**
     * 待质检数量
     */
    private Integer waitAmount;

    /**
     * 质检通过数量
     */
    private Integer passAmount;

    /**
     * 质检不通过数量
     */
    private Integer notPassAmount;

    /**
     * 质检结果：合格、不合格
     */
    private QcResult qcResult;

    /**
     * 质检不合格原因
     */
    private String qcNotPassedReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片id,多个用英文逗号分隔
     */
    private String pictureIds;

    /**
     * 重量,g
     */
    private BigDecimal weight;

    /**
     * 不合格明细的明细ID
     */
    private Long relationQcDetailId;

    /**
     * 创建人名称
     */
    private String createUsername;

    /**
     * 更新人名称
     */
    private String updateUsername;

    @JsonProperty("platCode")
    @ApiModelProperty(value = "平台")
    private String platform;

}
