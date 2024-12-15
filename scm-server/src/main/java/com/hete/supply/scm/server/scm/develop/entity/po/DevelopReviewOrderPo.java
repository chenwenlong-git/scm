package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import com.hete.supply.scm.server.scm.develop.enums.DevelopReviewRelated;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 开发审版单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_review_order")
@ApiModel(value = "DevelopReviewOrderPo对象", description = "开发审版单表")
public class DevelopReviewOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_review_order_id", type = IdType.ASSIGN_ID)
    private Long developReviewOrderId;


    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;


    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "审版单状态")
    private DevelopReviewOrderStatus developReviewOrderStatus;


    @ApiModelProperty(value = "审版单类型")
    private DevelopReviewOrderType developReviewOrderType;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;


    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;


    @ApiModelProperty(value = "关联单据")
    private String relateBizNo;


    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;


    @ApiModelProperty(value = "样品数量")
    private Integer developSampleNum;


    @ApiModelProperty(value = "审版人")
    private String reviewUser;


    @ApiModelProperty(value = "审版人")
    private String reviewUsername;


    @ApiModelProperty(value = "审版时间")
    private LocalDateTime reviewDate;


    @ApiModelProperty(value = "产前样单号")
    private String prenatalSampleOrderNo;

    @ApiModelProperty(value = "提交审版人")
    private String submitReviewUser;

    @ApiModelProperty(value = "提交审版人")
    private String submitReviewUsername;

    @ApiModelProperty(value = "提交审版时间")
    private LocalDateTime submitReviewDate;

    @ApiModelProperty(value = "不良数量")
    private Integer poorAmount;

    @ApiModelProperty(value = "审版关联单据类型")
    private DevelopReviewRelated developReviewRelated;
}
