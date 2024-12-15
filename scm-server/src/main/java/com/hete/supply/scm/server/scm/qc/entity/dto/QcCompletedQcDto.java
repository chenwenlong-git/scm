package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.supply.scm.server.scm.qc.enums.QcOperate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/10/13 17:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcCompletedQcDto extends QcSubmitDto {

    @NotNull(message = "质检操作不能为空")
    @ApiModelProperty(value = "质检操作")
    private QcOperate qcOperate;
}
