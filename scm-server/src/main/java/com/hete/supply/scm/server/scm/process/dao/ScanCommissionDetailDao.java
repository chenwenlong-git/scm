package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessUserPo;
import com.hete.supply.scm.server.scm.process.entity.po.ScanCommissionDetailPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 工序扫码提成明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-14
 */
@Component
@Validated
public class ScanCommissionDetailDao extends BaseDao<ScanCommissionDetailMapper, ScanCommissionDetailPo> {

    public List<ScanCommissionDetailPo> listByProcessOrderScanIds(Collection<Long> processOrderScanIds) {
        if (CollectionUtils.isEmpty(processOrderScanIds)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ScanCommissionDetailPo>lambdaQuery()
                .in(ScanCommissionDetailPo::getProcessOrderScanId, processOrderScanIds));
    }

    public List<ScanCommissionDetailPo> listByProcessOrderScanId(Long processOrderScanId) {
        if (Objects.isNull(processOrderScanId)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ScanCommissionDetailPo>lambdaQuery()
                .eq(ScanCommissionDetailPo::getProcessOrderScanId, processOrderScanId));
    }

    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
