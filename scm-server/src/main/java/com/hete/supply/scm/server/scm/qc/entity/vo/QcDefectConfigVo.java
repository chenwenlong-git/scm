package com.hete.supply.scm.server.scm.qc.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DefectStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/13 10:19
 */
@Data
@NoArgsConstructor
public class QcDefectConfigVo {
    @ApiModelProperty(value = "主键id")
    private Long qcOrderDefectConfigId;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "次品类别")
    private String defectCategory;

    @ApiModelProperty(value = "次品代码")
    private String defectCode;

    @ApiModelProperty(value = "次品配置状态")
    private DefectStatus defectStatus;

    @ApiModelProperty(value = "父级id")
    private Long parentDefectConfigId;

    @ApiModelProperty(value = "子级list")
    private List<QcDefectConfigVo> childDefectConfigList;
}
