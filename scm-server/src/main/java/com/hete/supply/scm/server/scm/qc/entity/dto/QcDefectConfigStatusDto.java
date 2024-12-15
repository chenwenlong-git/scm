package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DefectStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/10/13 14:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcDefectConfigStatusDto extends QcDefectConfigIdAndVersionDto {

    @NotNull(message = "次品配置状态不能为空")
    @ApiModelProperty(value = "次品配置状态")
    private DefectStatus defectStatus;


}
