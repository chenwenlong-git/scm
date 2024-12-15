package com.hete.supply.scm.server.scm.qc.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DefectStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 质检次品配置
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_order_defect_config")
@ApiModel(value = "QcOrderDefectConfigPo对象", description = "质检次品配置")
public class QcOrderDefectConfigPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "qc_order_defect_config_id", type = IdType.ASSIGN_ID)
    private Long qcOrderDefectConfigId;


    @ApiModelProperty(value = "次品类别")
    private String defectCategory;


    @ApiModelProperty(value = "次品代码")
    private String defectCode;


    @ApiModelProperty(value = "次品配置状态")
    private DefectStatus defectStatus;

    @ApiModelProperty(value = "父级id")
    private Long parentDefectConfigId;

}
