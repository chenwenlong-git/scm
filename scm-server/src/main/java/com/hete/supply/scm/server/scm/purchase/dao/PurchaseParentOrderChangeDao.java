package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderChangePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collections;
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
public class PurchaseParentOrderChangeDao extends BaseDao<PurchaseParentOrderChangeMapper, PurchaseParentOrderChangePo> {

    public PurchaseParentOrderChangePo getByParentId(@NotNull Long purchaseParentOrderId) {
        return baseMapper.selectOne(Wrappers.<PurchaseParentOrderChangePo>lambdaQuery()
                .eq(PurchaseParentOrderChangePo::getPurchaseParentOrderId, purchaseParentOrderId));

    }

    public void deleteByParentId(Long purchaseParentOrderId) {
        if (null == purchaseParentOrderId) {
            return;
        }

        baseMapper.deleteSkipCheck(Wrappers.<PurchaseParentOrderChangePo>lambdaUpdate()
                .eq(PurchaseParentOrderChangePo::getPurchaseParentOrderId, purchaseParentOrderId));
    }

    public List<PurchaseParentOrderChangePo> getListByParentIdList(List<Long> purchaseParentIdList) {
        if (CollectionUtils.isEmpty(purchaseParentIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseParentOrderChangePo>lambdaQuery()
                .in(PurchaseParentOrderChangePo::getPurchaseParentOrderId, purchaseParentIdList));
    }
}
