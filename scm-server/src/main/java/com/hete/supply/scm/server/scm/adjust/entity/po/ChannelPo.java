package com.hete.supply.scm.server.scm.adjust.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 渠道表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("channel")
@ApiModel(value = "ChannelPo对象", description = "渠道表")
public class ChannelPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "channel_id", type = IdType.ASSIGN_ID)
    private Long channelId;


    @ApiModelProperty(value = "渠道名称")
    private String channelName;


    @ApiModelProperty(value = "状态:启用(TRUE)、禁用(FALSE)")
    private BooleanType channelStatus;


}
