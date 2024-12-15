package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseModifyOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购需求变更单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-25
 */
@Component
@Validated
public class PurchaseModifyOrderItemDao extends BaseDao<PurchaseModifyOrderItemMapper, PurchaseModifyOrderItemPo> {

    public List<PurchaseModifyOrderItemPo> getListByNoList(List<String> downReturnNoList) {
        if (CollectionUtils.isEmpty(downReturnNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseModifyOrderItemPo>lambdaQuery()
                .in(PurchaseModifyOrderItemPo::getDownReturnOrderNo, downReturnNoList));
    }
}
