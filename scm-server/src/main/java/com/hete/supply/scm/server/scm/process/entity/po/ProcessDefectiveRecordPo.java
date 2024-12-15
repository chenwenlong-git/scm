package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DefectiveHandleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DefectiveHandleType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessDefectiveRecordStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 加工单次品记录表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_defective_record")
@ApiModel(value = "ProcessDefectiveRecordPo对象", description = "加工单次品记录表")
public class ProcessDefectiveRecordPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_defective_record_id", type = IdType.ASSIGN_ID)
    private Long processDefectiveRecordId;

    @ApiModelProperty(value = "单号")
    private String processDefectiveRecordNo;

    @ApiModelProperty(value = "关联的加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "次品处理方式")
    private DefectiveHandleMethod defectiveHandleMethod;

    @ApiModelProperty(value = "次品处理类型")
    private DefectiveHandleType defectiveHandleType;

    @ApiModelProperty(value = "次品处理状态")
    private ProcessDefectiveRecordStatus processDefectiveRecordStatus;

    @ApiModelProperty(value = "负责人编号")
    private String principalUser;

    @ApiModelProperty(value = "负责人名称")
    private String principalUsername;

    @ApiModelProperty(value = "供应商编号")
    private String supplierUser;

    @ApiModelProperty(value = "供应商名称")
    private String supplierUsername;

    @ApiModelProperty(value = "关联的单据")
    private String relatedOrderNo;


}
