package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(value = "创建质检实体", description = "创建质检实体")
public class ProcessOrderCreateQcBo {
    @NotEmpty(message = "创建质检信息不能为空")
    @ApiModelProperty(value = "质检信息列表")
    private List<ProcessOrderCreateQcOrderBo> createQcOrderBos;
}
