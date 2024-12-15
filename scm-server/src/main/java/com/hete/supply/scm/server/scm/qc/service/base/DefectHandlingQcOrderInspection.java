package com.hete.supply.scm.server.scm.qc.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DefectHandlingQcOrderInspection 是处理带有缺陷信息的质检订单巡检的实现类，它继承自 AbstractQcOrderInspection。
 * 该类实现了巡检方法 inspectQcOrders，用于检查指定天数内的质检订单，同时检查质检订单中是否包含缺陷信息。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Slf4j
public class DefectHandlingQcOrderInspection extends AbstractQcOrderInspection {

    private final DefectHandlingDao defectHandlingDao;

    /**
     * 构造函数用于注入依赖的服务。
     *
     * @param qcOrderDao        质检订单数据访问对象
     * @param defectHandlingDao 缺陷处理数据访问对象
     */
    public DefectHandlingQcOrderInspection(QcOrderDao qcOrderDao,
                                           DefectHandlingDao defectHandlingDao) {
        super(qcOrderDao);
        this.defectHandlingDao = defectHandlingDao;
    }

    /**
     * 检查指定天数内的质检订单并验证缺陷信息。
     *
     * @param qcOrders 质检订单列表，包含需要进行质检检查的订单信息。
     */
    @Override
    protected void inspectQcOrders(List<QcOrderPo> qcOrders) {
        final Set<String> queryQcOrderNos = qcOrders.stream()
                .map(QcOrderPo::getQcOrderNo)
                .collect(Collectors.toSet());
        final List<DefectHandlingPo> defectHandlingPos = Optional.ofNullable(
                        defectHandlingDao.listByQcOrderNoList(queryQcOrderNos))
                .orElse(Collections.emptyList());
        final Set<String> existQcOrderNos = defectHandlingPos.stream()
                .map(DefectHandlingPo::getQcOrderNo)
                .collect(Collectors.toSet());
        queryQcOrderNos.removeAll(existQcOrderNos);

        // 丢失次品记录信息的质检单号列表
        final Set<String> missingDefectQcOrderNos = new HashSet<>(queryQcOrderNos);
        if (CollectionUtils.isNotEmpty(missingDefectQcOrderNos)) {
            for (String missingQcOrderNo : missingDefectQcOrderNos) {
                log.error("质检单巡检发现异常：{} 质检单号：{} ", "次品处理中质检单无次品记录信息!", missingQcOrderNo);
            }
        }
    }
}

