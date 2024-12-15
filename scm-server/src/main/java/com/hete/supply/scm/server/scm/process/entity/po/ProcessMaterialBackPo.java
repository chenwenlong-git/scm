package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.enums.BackStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 加工原料归还表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_material_back")
@ApiModel(value = "ProcessMaterialBackPo对象", description = "加工原料归还单")
public class ProcessMaterialBackPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_material_back_id", type = IdType.ASSIGN_ID)
    private Long processMaterialBackId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "关联的返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "消息key")
    private String messageKey;

    @ApiModelProperty(value = "收货单编号")
    private String receiptNo;

    @ApiModelProperty(value = "归还状态")
    private BackStatus backStatus;

    @ApiModelProperty(value = "收货人")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

}
