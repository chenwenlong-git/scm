package com.hete.supply.scm.server.scm.process.entity.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/21.
 */
@Data
public class ScanRecordDataStatisticsBo {
    private BigDecimal totalProcessCommission = BigDecimal.ZERO;
    private Integer totalQualityGoodsCnt = 0;
}
