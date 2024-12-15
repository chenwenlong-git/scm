package com.hete.supply.scm.server.scm.supplier.service.ref;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityRuleDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.*;
import com.hete.supply.scm.server.scm.supplier.handler.SupOpCapacityHandler;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierCapacityBaseService;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierCapacityBizService;
import com.hete.support.consistency.core.service.ConsistencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierCapacityRefService {

    private final ConsistencyService consistencyService;
    private final SupplierCapacityBaseService supplierCapacityBaseService;
    private final SupplierCapacityDao supplierCapacityDao;

    public void operateSupplierCapacityBatch(List<SupOpCapacityBo> supOpCapacityBos) {
        log.info("供应商产能变更:{}", supOpCapacityBos);
        SupOpCapacityBatchBo supOpCapacityBatchBo = new SupOpCapacityBatchBo(supOpCapacityBos);
        consistencyService.execAsyncTask(SupOpCapacityHandler.class, supOpCapacityBatchBo);
    }

    public List<SupplierCapacityResBo> querySupplierCapacityBatch(List<SupplierCapacityQueryBo> queryParams) {
        if (CollectionUtils.isEmpty(queryParams)) {
            log.info("供应商产能查询参数为空");
            return Collections.emptyList();
        }

        // 校验参数
        boolean existsNull = queryParams.stream()
                .anyMatch(queryParam -> null == queryParam.getCapacityDate() || null == queryParam.getSupplierCode());
        if (existsNull) {
            log.info("供应商产能查询参数日期/供应商编码为空");
            return Collections.emptyList();
        }

        // 获取供应商产能规则
        List<String> supplierCodeList
                = queryParams.stream().map(SupplierCapacityQueryBo::getSupplierCode).collect(Collectors.toList());
        Map<String, BigDecimal> supCapRuleMap = supplierCapacityBaseService.getSupCapRuleMap(supplierCodeList);

        List<SupplierCapacityResBo> supplierCapacityResBos = supplierCapacityDao.listBySupplierCodeAndDate(queryParams);
        return queryParams.stream().map(queryParam -> {
                    SupplierCapacityResBo supplierCapacityResBo = new SupplierCapacityResBo();
                    supplierCapacityResBo.setSupplierCode(queryParam.getSupplierCode());
                    supplierCapacityResBo.setCapacityDate(queryParam.getCapacityDate());
                    BigDecimal normalAvailableCapacity = supplierCapacityResBos.stream().filter(resBo -> resBo.getSupplierCode().equals(queryParam.getSupplierCode())
                                    && resBo.getCapacityDate().equals(queryParam.getCapacityDate()))
                            .map(SupplierCapacityResBo::getNormalAvailableCapacity)
                            .findFirst().orElse(supCapRuleMap.get(queryParam.getSupplierCode()));
                    supplierCapacityResBo.setNormalAvailableCapacity(normalAvailableCapacity);
                    return supplierCapacityResBo;
                    // 过滤掉空的剩余产能
                }).filter(resBo -> resBo.getNormalAvailableCapacity() != null)
                .collect(Collectors.toList());
    }
}

















