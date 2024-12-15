package com.hete.supply.scm.server.scm.process.entity.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/16.
 */
@Data
public class CommissionBo {
    private Long processOrderScanId;
    private BigDecimal totalCommission;
    private List<CommissionDetailBo> commissionDetails = Collections.emptyList();
}
