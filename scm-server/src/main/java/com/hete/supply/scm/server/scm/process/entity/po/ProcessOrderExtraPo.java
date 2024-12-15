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

import java.time.LocalDateTime;

/**
 * <p>
 * 加工单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_extra")
@ApiModel(value = "ProcessOrderExtraPo对象", description = "加工单")
public class ProcessOrderExtraPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_extra_id", type = IdType.ASSIGN_ID)
    private Long processOrderExtraId;


    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "投产人 id")
    private String producedUser;

    @ApiModelProperty(value = "投产人名称")
    private String producedUsername;

    @ApiModelProperty(value = "发货人 id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货单号")
    private String receiptOrderNo;


    @ApiModelProperty(value = "收货人 id")
    private String receiptUser;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "次品收货单号")
    private String defectiveReceiptOrderNo;

    @ApiModelProperty(value = "次品收货人 id")
    private String defectiveReceiptUser;

    @ApiModelProperty(value = "次品收货人名称")
    private String defectiveReceiptUsername;


    @ApiModelProperty(value = "入库单号")
    private String storeOrderNo;


    @ApiModelProperty(value = "入库人 id")
    private String storeUser;


    @ApiModelProperty(value = "入库人名称")
    private String storeUsername;


    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;


    @ApiModelProperty(value = "结算人 id")
    private String settleUser;


    @ApiModelProperty(value = "结算人名称")
    private String settleUsername;


    @ApiModelProperty(value = "结算时间")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "进入加工中操作人 id")
    private String processingUser;


    @ApiModelProperty(value = "进入加工中操作人名称")
    private String processingUsername;


    @ApiModelProperty(value = "进入加工中时间")
    private LocalDateTime processingTime;


    @ApiModelProperty(value = "完成扫码人 id")
    private String completeScanUser;


    @ApiModelProperty(value = "完成扫码人名称")
    private String completeScanUsername;


    @ApiModelProperty(value = "完成扫码时间")
    private LocalDateTime completeScanTime;


    @ApiModelProperty(value = "质检单号")
    private String checkOrderNo;

    @ApiModelProperty(value = "质检人")
    private String checkUser;

    @ApiModelProperty(value = "质检人")
    private String checkUsername;


    @ApiModelProperty(value = "后整质检中处理人 id")
    private String checkingUser;


    @ApiModelProperty(value = "后整质检中处理人名称")
    private String checkingUsername;


    @ApiModelProperty(value = "后整质检中处理时间")
    private LocalDateTime checkingTime;

}
