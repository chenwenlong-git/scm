package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.OnShelvesOrderBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.service.biz.OnShelvesOrderBizService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * FinishedQcOrderInspection 是已完成质检订单巡检的实现类，它继承自 AbstractQcOrderInspection。
 * 该类实现了巡检方法 inspectQcOrders，用于检查指定天数内的质检订单，并验证是否包含上架单信息、次品处理信息和退货单信息。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Slf4j
public class FinishedQcOrderInspection extends AbstractQcOrderInspection {
    private final OnShelvesOrderBizService onShelvesOrderBizService;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final DefectHandlingDao defectHandlingDao;
    private final QcOrderBaseService qcOrderBaseService;

    /**
     * 构造函数用于注入依赖的服务。
     *
     * @param qcOrderDao                 质检订单数据访问对象
     * @param defectHandlingDao          缺陷处理数据访问对象
     * @param onShelvesOrderBizService   上架单业务服务
     * @param purchaseReturnOrderItemDao 退货订单项数据访问对象
     */
    public FinishedQcOrderInspection(QcOrderDao qcOrderDao,
                                     DefectHandlingDao defectHandlingDao,
                                     OnShelvesOrderBizService onShelvesOrderBizService,
                                     PurchaseReturnOrderItemDao purchaseReturnOrderItemDao,
                                     QcOrderBaseService qcOrderBaseService) {
        super(qcOrderDao);
        this.defectHandlingDao = defectHandlingDao;
        this.onShelvesOrderBizService = onShelvesOrderBizService;
        this.purchaseReturnOrderItemDao = purchaseReturnOrderItemDao;
        this.qcOrderBaseService = qcOrderBaseService;
    }

    /**
     * 检查指定天数内的质检订单并验证上架单信息、次品处理信息和退货单信息。
     *
     * @param qcOrders 质检订单列表，包含需要进行质检检查的订单信息。
     */
    @Override
    protected void inspectQcOrders(List<QcOrderPo> qcOrders) {
        List<QcOrigin> residentQcOrigins = qcOrderBaseService.getResidentQcOrigins();
        qcOrders.removeIf(qcOrderPo -> residentQcOrigins.contains(qcOrderPo.getQcOrigin()));
        if (CollectionUtils.isEmpty(qcOrders)) {
            return;
        }

        final Set<String> allQcOrderNos = qcOrders.stream()
                .map(QcOrderPo::getQcOrderNo)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());

        // 获取质检单号列表上架单信息
        final List<OnShelvesOrderBo> onShelvesOrderBos = Optional.ofNullable(
                        onShelvesOrderBizService.getOnShelvesOrderByQcOrderNos(allQcOrderNos))
                .orElse(Collections.emptyList());
        final Set<String> existOnShelvesQcOrderNos = onShelvesOrderBos.stream()
                .map(OnShelvesOrderBo::getQcOrderNo)
                .collect(Collectors.toSet());

        // 通过质检单号列表获取次品处理信息
        final List<DefectHandlingPo> defectHandlingPos = Optional.ofNullable(
                        defectHandlingDao.listByQcOrderNoList(allQcOrderNos))
                .orElse(Collections.emptyList());
        final Set<String> existDefectQcOrderNos = defectHandlingPos.stream()
                .map(DefectHandlingPo::getQcOrderNo)
                .collect(Collectors.toSet());

        // 通过质检单号列表获取退货订单信息
        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPos = Optional.ofNullable(
                        purchaseReturnOrderItemDao.getListByReturnBizNos(allQcOrderNos))
                .orElse(Collections.emptyList());
        final Set<String> existReturnQcOrderNos = purchaseReturnOrderItemPos.stream()
                .map(PurchaseReturnOrderItemPo::getReturnBizNo)
                .collect(Collectors.toSet());

        allQcOrderNos.removeAll(existOnShelvesQcOrderNos);
        allQcOrderNos.removeAll(existDefectQcOrderNos);
        allQcOrderNos.removeAll(existReturnQcOrderNos);
        final Set<String> errorQcOrderNos = new HashSet<>(allQcOrderNos);
        if (CollectionUtils.isNotEmpty(errorQcOrderNos)) {
            log.error("质检单巡检发现异常：{} 质检单号：{} ", "已完结质检单无上架单信息、次品记录信息、退货信息!",
                    errorQcOrderNos.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")));
        }
    }
}

