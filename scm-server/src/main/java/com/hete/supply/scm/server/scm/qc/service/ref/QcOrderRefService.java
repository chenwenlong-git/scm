package com.hete.supply.scm.server.scm.qc.service.ref;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcOrderBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/11/3 15:57
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QcOrderRefService {
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final QcReceiveOrderDao qcReceiveOrderDao;

    public Map<String, List<QcOrderBo>> getReceiveQcOrderByReceiveOrderNoList(List<@NotBlank(message = "收货单列表不能为空") String>
                                                                                      receiveOrderNoList) {
        if (CollectionUtils.isEmpty(receiveOrderNoList)) {
            return Collections.emptyMap();
        }

        final List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getByReceiveNos(receiveOrderNoList);
        if (CollectionUtils.isEmpty(qcReceiveOrderPoList)) {
            return Collections.emptyMap();
        }

        final List<String> qcOrderNoList = qcReceiveOrderPoList.stream()
                .map(QcReceiveOrderPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<QcOrderPo> qcOrderPoList = qcOrderDao.getByQcOrderNos(qcOrderNoList);
        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNoList(qcOrderNoList);

        final Map<String, QcOrderPo> qcOrderNoPoMap = qcOrderPoList.stream()
                .collect(Collectors.toMap(QcOrderPo::getQcOrderNo, Function.identity()));
        final Map<String, List<QcDetailPo>> qcOrderNoDetailListMap = qcDetailPoList.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo));

        Map<String, List<QcOrderBo>> resultMap = new HashMap<>();

        for (QcReceiveOrderPo qcReceiveOrderPo : qcReceiveOrderPoList) {
            final QcOrderBo qcOrderBo = new QcOrderBo();
            final QcOrderPo qcOrderPo = qcOrderNoPoMap.get(qcReceiveOrderPo.getQcOrderNo());
            if (null == qcOrderPo) {
                continue;
            }
            qcOrderBo.setQcOrderPo(qcOrderPo);
            qcOrderBo.setQcDetailPoList(qcOrderNoDetailListMap.get(qcReceiveOrderPo.getQcOrderNo()));
            if (resultMap.containsKey(qcReceiveOrderPo.getReceiveOrderNo())) {
                resultMap.get(qcReceiveOrderPo.getReceiveOrderNo()).add(qcOrderBo);
            } else {
                resultMap.put(qcReceiveOrderPo.getReceiveOrderNo(), Lists.newArrayList(qcOrderBo));
            }

        }

        return resultMap;
    }

    /**
     * 将采购订单类型映射到质检原产地属性。
     *
     * @param purchaseOrderType 采购订单类型
     * @return 对应的质检原产地属性
     * @throws IllegalArgumentException 如果采购订单类型无法映射到质检原产地属性时，抛出该异常
     */
    public QcOriginProperty mapPurchaseOrderTypeToQcOriginProperty(PurchaseOrderType purchaseOrderType) {
        if (PurchaseOrderType.FIRST_ORDER.equals(purchaseOrderType)) {
            return QcOriginProperty.FIRST_ORDER;
        } else if (PurchaseOrderType.NORMAL.equals(purchaseOrderType)) {
            return QcOriginProperty.NORMAL;
        } else if (PurchaseOrderType.PRENATAL.equals(purchaseOrderType)) {
            return QcOriginProperty.PRENATAL;
        } else if (PurchaseOrderType.WH.equals(purchaseOrderType)) {
            return QcOriginProperty.WH;
        } else if (PurchaseOrderType.REPAIR.equals(purchaseOrderType)) {
            return QcOriginProperty.REPAIR;
        } else if (PurchaseOrderType.SPECIAL.equals(purchaseOrderType)) {
            return QcOriginProperty.SPECIAL;
        }
        throw new ParamIllegalException("无法通过采购订单类型匹配对应质检单来源，请配置:{}", purchaseOrderType);
    }
}
