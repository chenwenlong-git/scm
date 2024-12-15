package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工序用户绑定关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_user")
@ApiModel(value = "ProcessUserPo对象", description = "工序用户绑定关系表")
public class ProcessUserPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_user_id", type = IdType.ASSIGN_ID)
    private Long processUserId;

    @ApiModelProperty(value = "工序 ID")
    private Long processId;

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String username;

}
