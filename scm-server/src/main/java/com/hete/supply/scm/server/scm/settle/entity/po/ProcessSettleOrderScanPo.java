package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 加工结算单明细工序扫码记录
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_settle_order_scan")
@ApiModel(value = "ProcessSettleOrderScanPo对象", description = "加工结算单明细工序扫码记录")
public class ProcessSettleOrderScanPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_settle_order_scan_id", type = IdType.ASSIGN_ID)
    private Long processSettleOrderScanId;


    @ApiModelProperty(value = "关联结算单详情ID")
    private Long processSettleOrderItemId;

    @ApiModelProperty(value = "扫码记录id")
    private Long processOrderScanId;

    @ApiModelProperty(value = "总提成")
    private BigDecimal totalProcessCommission;


    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;


    @ApiModelProperty(value = "关联的工序代码")
    private String processCode;


    @ApiModelProperty(value = "关联的工序名称")
    private String processName;

    @ApiModelProperty(value = "关联的工序提成")
    private BigDecimal processCommission;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "下单人")
    private String orderUser;


    @ApiModelProperty(value = "下单人名称")
    private String orderUsername;


    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;


    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

}
