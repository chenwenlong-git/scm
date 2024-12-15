package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.supply.scm.server.scm.qc.enums.QcApproveOperate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/18 14:26
 */
@Data
@NoArgsConstructor
public class QcApproveDto {
    @NotNull(message = "质检审核操作不能为空")
    @ApiModelProperty(value = "质检审核操作")
    private QcApproveOperate qcApproveOperate;

    @NotEmpty(message = "质检单号不能为空")
    @ApiModelProperty(value = "质检单号")
    private List<String> qcOrderNoList;
}
