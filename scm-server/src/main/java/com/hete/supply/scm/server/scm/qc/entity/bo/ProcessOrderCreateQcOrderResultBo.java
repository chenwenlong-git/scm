package com.hete.supply.scm.server.scm.qc.entity.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
public class ProcessOrderCreateQcOrderResultBo {
    @NotBlank(message = "容器编码列表")
    private Set<String> containerCodeList;

    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;

    @NotNull(message = "质检单号不能为空")
    private String qcOrderNo;

    @NotBlank(message = "操作人编码")
    private String operator;

    @NotBlank(message = "操作人姓名")
    private String operatorName;
}
