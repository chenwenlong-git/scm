package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.bo.CommissionDetailBo;
import com.hete.supply.scm.server.scm.process.entity.po.ScanCommissionDetailPo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/18.
 */
public class CommissionDetailConverter {

    public static List<CommissionDetailBo> convertToCommissionDetailBoList(List<ScanCommissionDetailPo> scanCommissionDetailPos) {
        if (CollectionUtils.isEmpty(scanCommissionDetailPos)) {
            return Collections.emptyList();
        }

        return scanCommissionDetailPos.stream()
                .map(scanCommissionDetailPo -> {
                    CommissionDetailBo commissionDetailBo = new CommissionDetailBo();
                    commissionDetailBo.setScanCommissionDetailId(scanCommissionDetailPo.getScanCommissionDetailId());
                    commissionDetailBo.setProcessOrderScanId(scanCommissionDetailPo.getProcessOrderScanId());
                    commissionDetailBo.setCommissionCategory(scanCommissionDetailPo.getCommissionCategory());
                    commissionDetailBo.setCommissionAttribute(scanCommissionDetailPo.getCommissionAttribute());
                    commissionDetailBo.setQuantity(scanCommissionDetailPo.getQuantity());
                    commissionDetailBo.setUnitCommission(scanCommissionDetailPo.getUnitCommission());
                    commissionDetailBo.setTotalAmount(scanCommissionDetailPo.getTotalAmount());
                    return commissionDetailBo;
                })
                .collect(Collectors.toList());
    }
}
