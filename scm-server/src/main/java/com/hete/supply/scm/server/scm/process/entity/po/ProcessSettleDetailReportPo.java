package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.support.mybatis.plus.entity.po.BasePo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.tomcat.jni.Proc;

/**
 * <p>
 * 加工结算详情报表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_settle_detail_report")
@ApiModel(value = "ProcessSettleDetailReportPo对象", description = "加工结算详情报表")
public class ProcessSettleDetailReportPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_settle_detail_report_id", type = IdType.ASSIGN_ID)
    private Long processSettleDetailReportId;


    @ApiModelProperty(value = "结算单主键id")
    private Long processSettleOrderId;


    @ApiModelProperty(value = "结算单明细主键id")
    private Long processSettleOrderItemId;


    @ApiModelProperty(value = "完成人的用户")
    private String completeUser;


    @ApiModelProperty(value = "完成人的用户名")
    private String completeUsername;


    @ApiModelProperty(value = "工序代码")
    private String processCode;


    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;


    @ApiModelProperty(value = "工序名称")
    private String processName;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "一级提成正品数量")
    private Integer firstLevelQualityGoodsCnt;


    @ApiModelProperty(value = "一级提成总金额")
    private BigDecimal firstLevelTotalCommission;


    @ApiModelProperty(value = "二级提成正品数量")
    private Integer secondLevelQualityGoodsCnt;


    @ApiModelProperty(value = "二级提成总金额")
    private BigDecimal secondLevelTotalCommission;


}
