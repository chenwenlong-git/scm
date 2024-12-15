package com.hete.supply.scm.server.scm.qc.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 质检单详情
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_detail")
@ApiModel(value = "QcDetailPo对象", description = "质检单详情")
public class QcDetailPo extends BaseSupplyPo {


    @ApiModelProperty(value = "质检单详情id")
    @TableId(value = "qc_detail_id", type = IdType.ASSIGN_ID)
    private Long qcDetailId;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "平台")
    private String platform;


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


    @ApiModelProperty(value = "质检结果")
    private QcResult qcResult;


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

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "交接数量")
    private Integer handOverAmount;

}
