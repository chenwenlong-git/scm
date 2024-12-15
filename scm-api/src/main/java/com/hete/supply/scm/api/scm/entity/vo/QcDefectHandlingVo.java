package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2023/12/7.
 */
@Data
public class QcDefectHandlingVo {
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;
    @ApiModelProperty(value = "让步上架总数")
    private int concessionShelfTotal;
}
