package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
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
 * 工序扫码单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_scan")
@ApiModel(value = "ProcessOrderScanPo对象", description = "工序扫码单")
public class ProcessOrderScanPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_scan_id", type = IdType.ASSIGN_ID)
    private Long processOrderScanId;

    @ApiModelProperty(value = "关联的加工工序id")
    private Long processOrderProcedureId;

    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "一级工序")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "关联的工序代码")
    private String processCode;


    @ApiModelProperty(value = "关联的工序名称")
    private String processName;


    @ApiModelProperty(value = "关联的工序提成")
    private BigDecimal processCommission;

    @ApiModelProperty(value = "额外提成单价")
    private BigDecimal extraCommission;

    @ApiModelProperty(value = "接货数量")
    private Integer receiptNum;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "下单时间")
    private LocalDateTime orderTime;


    @ApiModelProperty(value = "下单人")
    private String orderUser;


    @ApiModelProperty(value = "下单人名称")
    private String orderUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "接货人 id")
    private String receiptUser;


    @ApiModelProperty(value = "接货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "加工时间")
    private LocalDateTime processingTime;


    @ApiModelProperty(value = "加工人 id")
    private String processingUser;


    @ApiModelProperty(value = "加工人名称")
    private String processingUsername;


    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;


    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;


    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

}
