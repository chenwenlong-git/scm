package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 开发子单变更记录表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_child_order_change")
@ApiModel(value = "DevelopChildOrderChangePo对象", description = "开发子单变更记录表")
public class DevelopChildOrderChangePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_child_order_change_id", type = IdType.ASSIGN_ID)
    private Long developChildOrderChangeId;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发人")
    private String devUser;


    @ApiModelProperty(value = "开发人")
    private String devUsername;


    @ApiModelProperty(value = "跟单人")
    private String followUser;


    @ApiModelProperty(value = "跟单人")
    private String followUsername;


    @ApiModelProperty(value = "审版人")
    private String reviewUser;


    @ApiModelProperty(value = "审版人")
    private String reviewUsername;


    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUser;


    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUsername;


    @ApiModelProperty(value = "打版完成时间")
    private LocalDateTime pamphletCompletionDate;


    @ApiModelProperty(value = "跟单签收时间")
    private LocalDateTime followDate;


    @ApiModelProperty(value = "审版完成时间")
    private LocalDateTime reviewCompletionDate;


    @ApiModelProperty(value = "核价完成时间")
    private LocalDateTime pricingCompletionDate;


    @ApiModelProperty(value = "上新完成时间")
    private LocalDateTime newestCompletionDate;


    @ApiModelProperty(value = "上架完成时间")
    private LocalDateTime onShelvesCompletionDate;


}
