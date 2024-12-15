package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
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
 * 补款单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplement_order")
@ApiModel(value = "SupplementOrderPo对象", description = "补款单")
public class SupplementOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplement_order_id", type = IdType.ASSIGN_ID)
    private Long supplementOrderId;


    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;


    @ApiModelProperty(value = "补款类型")
    private SupplementType supplementType;


    @ApiModelProperty(value = "补款状态")
    private SupplementStatus supplementStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "补款员工")
    private String supplementUser;


    @ApiModelProperty(value = "补款员工用户名")
    private String supplementUsername;


    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;


    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;


    @ApiModelProperty(value = "确认拒绝备注")
    private String confirmRefuseRemarks;


    @ApiModelProperty(value = "审核拒绝备注")
    private String examineRefuseRemarks;


    @ApiModelProperty(value = "提交人的用户")
    private String submitUser;


    @ApiModelProperty(value = "提交人的用户名")
    private String submitUsername;


    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;


    @ApiModelProperty(value = "确认人的用户")
    private String confirmUser;


    @ApiModelProperty(value = "确认人的用户名")
    private String confirmUsername;


    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;


    @ApiModelProperty(value = "审核人的用户")
    private String examineUser;


    @ApiModelProperty(value = "审核人的用户名")
    private String examineUsername;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime examineTime;


    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "关联采购/加工结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "价格拒绝备注")
    private String priceRefuseRemarks;

    @ApiModelProperty(value = "价格确认人的用户")
    private String priceConfirmUser;

    @ApiModelProperty(value = "价格确认人的用户名")
    private String priceConfirmUsername;

    @ApiModelProperty(value = "价格确认时间")
    private LocalDateTime priceConfirmTime;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;


}
