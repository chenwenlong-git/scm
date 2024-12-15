package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/11/3 16:08
 */
@Data
@NoArgsConstructor
public class QcOrderBo {
    @ApiModelProperty(value = "质检单")
    private QcOrderPo qcOrderPo;

    @ApiModelProperty(value = "质检单详情")
    private List<QcDetailPo> qcDetailPoList;
}
