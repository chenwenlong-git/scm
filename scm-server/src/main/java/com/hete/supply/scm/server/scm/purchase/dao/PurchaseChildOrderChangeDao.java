package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderChangePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 采购需求母单变更时间记录 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Component
@Validated
public class PurchaseChildOrderChangeDao extends BaseDao<PurchaseChildOrderChangeMapper, PurchaseChildOrderChangePo> {

    public List<PurchaseChildOrderChangePo> getByChildOrderId(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderChangePo>lambdaQuery()
                .in(PurchaseChildOrderChangePo::getPurchaseChildOrderId, idList));

    }

    public PurchaseChildOrderChangePo getByChildOrderId(Long purchaseChildOrderId) {
        if (null == purchaseChildOrderId) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseChildOrderChangePo>lambdaQuery()
                .eq(PurchaseChildOrderChangePo::getPurchaseChildOrderId, purchaseChildOrderId));
    }

    public void deleteByChildIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseChildOrderChangePo>lambdaUpdate()
                .in(PurchaseChildOrderChangePo::getPurchaseChildOrderId, idList));
    }

    /**
     * 通过发货单号批量查询
     *
     * @author ChenWenLong
     * @date 2023/4/10 10:43
     */
    public List<PurchaseChildOrderChangePo> getBatchPurchaseDeliverOrderNo(List<String> purchaseDeliverOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseDeliverOrderNoList)) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderChangePo>lambdaQuery()
                .in(PurchaseChildOrderChangePo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNoList));

    }

    public List<PurchaseChildOrderChangePo> getListByChildNoList(List<String> purchaseChildOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderChangePo>lambdaQuery()
                .in(PurchaseChildOrderChangePo::getPurchaseChildOrderNo, purchaseChildOrderNoList));
    }
}
