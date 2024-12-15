package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseModifyOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购需求变更单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-25
 */
@Component
@Validated
public class PurchaseModifyOrderDao extends BaseDao<PurchaseModifyOrderMapper, PurchaseModifyOrderPo> {

    public List<PurchaseModifyOrderPo> getByChildOrderNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseModifyOrderPo>lambdaQuery()
                .in(PurchaseModifyOrderPo::getPurchaseChildOrderNo, childOrderNoList)
                .orderByDesc(PurchaseModifyOrderPo::getCreateTime));
    }

    public PurchaseModifyOrderPo getOneByChildOrderNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseModifyOrderPo>lambdaQuery()
                .eq(PurchaseModifyOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }
}
