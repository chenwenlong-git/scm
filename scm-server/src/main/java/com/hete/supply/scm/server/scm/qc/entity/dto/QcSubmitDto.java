package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/12 16:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcSubmitDto extends QcOrderIdAndVersionDto {
    @ApiModelProperty(value = "正品信息")
    @NotEmpty(message = "正品信息不能为空")
    @Valid
    private List<QcDetailHandItemDto> qcDetailHandItemList;


    @ApiModelProperty(value = "次品信息")
    @Valid
    private List<QcUnPassDetailItemDto> qcUnPassDetailItemList;
}
