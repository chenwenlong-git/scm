package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPayPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购结算单支付明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-08
 */
@Component
@Validated
public class PurchaseSettleOrderPayDao extends BaseDao<PurchaseSettleOrderPayMapper, PurchaseSettleOrderPayPo> {

    /**
     * 根据采购结算单ID查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 14:49
     */
    public List<PurchaseSettleOrderPayPo> getByPurchaseSettleOrderId(Long purchaseSettleOrderId) {
        return list(Wrappers.<PurchaseSettleOrderPayPo>lambdaQuery().eq(PurchaseSettleOrderPayPo::getPurchaseSettleOrderId, purchaseSettleOrderId));
    }

    /**
     * 根据ID和版本号查询
     *
     * @author ChenWenLong
     * @date 2022/11/9 16:36
     */
    public PurchaseSettleOrderPayPo getByPurchaseSettleOrderIdAndVersion(Long purchaseSettleOrderPayId, Integer version) {
        return baseMapper.selectByIdVersion(purchaseSettleOrderPayId, version);
    }

    /**
     * 通过批量结算单ID查询获取map集合
     *
     * @author ChenWenLong
     * @date 2023/4/3 10:43
     */
    public Map<Long, List<PurchaseSettleOrderPayPo>> getBatchPurchaseSettleOrderId(List<Long> purchaseSettleOrderIdList) {
        if (CollectionUtils.isEmpty(purchaseSettleOrderIdList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<PurchaseSettleOrderPayPo>lambdaQuery()
                .in(PurchaseSettleOrderPayPo::getPurchaseSettleOrderId, purchaseSettleOrderIdList))
                .stream().collect(Collectors.groupingBy(PurchaseSettleOrderPayPo::getPurchaseSettleOrderId));
    }

}
