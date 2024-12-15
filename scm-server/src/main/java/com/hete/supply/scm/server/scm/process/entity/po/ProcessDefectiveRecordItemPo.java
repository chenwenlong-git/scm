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
 * 加工单次品记录明细表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_defective_record_item")
@ApiModel(value = "ProcessDefectiveRecordItemPo对象", description = "加工单次品记录明细表")
public class ProcessDefectiveRecordItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_defective_record_item_id", type = IdType.ASSIGN_ID)
    private Long processDefectiveRecordItemId;

    @ApiModelProperty(value = "关联的次品记录")
    private String processDefectiveRecordNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "不良原因")
    private String badReason;


}
