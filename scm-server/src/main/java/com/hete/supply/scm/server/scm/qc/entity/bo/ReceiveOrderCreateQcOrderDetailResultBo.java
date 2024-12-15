package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
public class ReceiveOrderCreateQcOrderDetailResultBo {
    @ApiModelProperty(value = "质检单详情id")
    private Long qcDetailId;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "赫特spu")
    private String spu;

    @ApiModelProperty(value = "赫特sku")
    private String skuCode;

    @ApiModelProperty(value = "(应)质检总数量")
    private Integer amount;

    @ApiModelProperty(value = "待质检数量")
    private Integer waitAmount;

    @ApiModelProperty(value = "质检通过数量")
    private Integer passAmount;

    @ApiModelProperty(value = "质检不通过数量")
    private Integer notPassAmount;

    @ApiModelProperty(value = "质检结果：合格、不合格")
    private QcResult qcResult;

    @ApiModelProperty(value = "质检不合格原因")
    private String qcNotPassedReason;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "图片id,多个用英文逗号分隔")
    private String pictureIds;

    @ApiModelProperty(value = "重量,g")
    private BigDecimal weight;

    @ApiModelProperty(value = "不合格明细的明细ID")
    private Long relationQcDetailId;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;
}
