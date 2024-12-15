package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DefectStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenWenLong
 * @date 2024/1/2 11:55
 */
@Data
public class QcOrderDefectConfigExportVo {

    @ApiModelProperty(value = "次品类别")
    private String defectCategory;

    @ApiModelProperty(value = "次品代码")
    private String defectCode;

    @ApiModelProperty(value = "次品原因")
    private String defectReason;

    @ApiModelProperty(value = "原因代码")
    private String reasonCode;

    @ApiModelProperty(value = "状态")
    private DefectStatus defectStatus;

    @ApiModelProperty(value = "状态名称")
    private String defectStatusName;

}
