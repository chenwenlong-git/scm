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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 工序扫码关联表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_scan_relate")
@ApiModel(value = "ProcessOrderScanRelatePo对象", description = "工序扫码关联表")
public class ProcessOrderScanRelatePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_scan_relate_id", type = IdType.ASSIGN_ID)
    private Long processOrderScanRelateId;


    @ApiModelProperty(value = "process_order_scan表主键id")
    private Long processOrderScanId;


    @ApiModelProperty(value = "关联的工序代码")
    private String processCode;


    @ApiModelProperty(value = "关联的工序提成")
    private BigDecimal processCommission;


    @ApiModelProperty(value = "额外提成单价")
    private BigDecimal extraCommission;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;


    @ApiModelProperty(value = "扫码完成人 id")
    private String completeUser;


    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;
}
