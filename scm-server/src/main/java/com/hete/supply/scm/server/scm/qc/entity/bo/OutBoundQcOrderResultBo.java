package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/7/2.
 */
@Data
@AllArgsConstructor
public class OutBoundQcOrderResultBo {
    private QcOrderPo qcOrderPo;
    private List<QcDetailPo> qcDetailPoList;
}
