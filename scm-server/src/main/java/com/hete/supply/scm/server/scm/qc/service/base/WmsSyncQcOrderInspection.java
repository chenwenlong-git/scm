package com.hete.supply.scm.server.scm.qc.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.wms.api.entry.entity.vo.QcOrderToScmVo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WmsSyncQcOrderInspection 是处理质检订单巡检的实现类，它继承自 AbstractQcOrderInspection。
 * 该类实现了巡检方法 inspectQcOrders，用于检查指定天数内的质检订单，同时检查仓储服务的质检信息是否存在。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Slf4j
public class WmsSyncQcOrderInspection extends AbstractQcOrderInspection {

    private final WmsRemoteService wmsRemoteService;
    private final QcOrderBaseService qcOrderBaseService;

    /**
     * 构造函数用于注入依赖的服务。
     *
     * @param qcOrderDao 质检订单数据访问对象
     */
    public WmsSyncQcOrderInspection(QcOrderDao qcOrderDao,
                                    WmsRemoteService wmsRemoteService,
                                    QcOrderBaseService qcOrderBaseService) {
        super(qcOrderDao);
        this.wmsRemoteService = wmsRemoteService;
        this.qcOrderBaseService = qcOrderBaseService;
    }

    /**
     * 检查指定天数内的质检订单并验证缺陷信息。
     *
     * @param qcOrders 质检订单列表，包含需要进行质检检查的订单信息。
     */
    @Override
    protected void inspectQcOrders(List<QcOrderPo> qcOrders) {
        List<QcOrigin> residentQcOrigins
                = qcOrderBaseService.getResidentQcOrigins();
        qcOrders.removeIf(qcOrderPo -> residentQcOrigins.contains(qcOrderPo.getQcOrigin()));
        if (CollectionUtils.isEmpty(qcOrders)) {
            return;
        }

        final Set<String> queryQcOrderNos = qcOrders.stream()
                .map(QcOrderPo::getQcOrderNo)
                .collect(Collectors.toSet());
        final List<QcOrderToScmVo> wmsQcOrderByQcOrderNos
                = wmsRemoteService.getWmsQcOrderByQcOrderNos(queryQcOrderNos);

        final Set<String> existQcOrderNos = wmsQcOrderByQcOrderNos.stream()
                .map(QcOrderToScmVo::getQcOrderNo)
                .collect(Collectors.toSet());
        queryQcOrderNos.removeAll(existQcOrderNos);

        // 丢失次品记录信息的质检单号列表
        final Set<String> missingDefectQcOrderNos = new HashSet<>(queryQcOrderNos);
        if (CollectionUtils.isNotEmpty(missingDefectQcOrderNos)) {
            for (String missingQcOrderNo : missingDefectQcOrderNos) {
                log.error("质检单巡检发现异常：{} 质检单号：{} ", "存在供应链但不存在仓储服务质检单信息!", missingQcOrderNo);
            }
        }
    }
}

