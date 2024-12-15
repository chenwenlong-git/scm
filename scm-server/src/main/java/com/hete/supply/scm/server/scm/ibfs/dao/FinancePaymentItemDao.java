package com.hete.supply.scm.server.scm.ibfs.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePaymentItemPo;
import com.hete.supply.scm.server.scm.ibfs.enums.PaymentBizType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务相关单付款明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-14
 */
@Component
@Validated
public class FinancePaymentItemDao extends BaseDao<FinancePaymentItemMapper, FinancePaymentItemPo> {

    public List<FinancePaymentItemPo> getListByBizNoAndType(String paymentBizNo,
                                                            PaymentBizType paymentBizType) {
        if (StringUtils.isBlank(paymentBizNo) || null == paymentBizType) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinancePaymentItemPo>lambdaQuery()
                .eq(FinancePaymentItemPo::getPaymentBizNo, paymentBizNo)
                .eq(FinancePaymentItemPo::getPaymentBizType, paymentBizType));
    }

    /**
     * 查询付款信息列表，通过结算单号
     *
     * @param financeSettleOrderNo 结算单号
     * @return 付款信息列表
     */
    public List<FinancePaymentItemPo> findBySettleOrderNo(String financeSettleOrderNo) {
        if (StrUtil.isBlank(financeSettleOrderNo)) {
            return Collections.emptyList();
        }
        return getListByBizNoAndType(financeSettleOrderNo, PaymentBizType.SETTLEMENT);
    }

    /**
     * 查询付款信息列表，通过结算单号
     *
     * @param financeSettleOrderNos 结算单号
     * @return 付款信息列表
     */
    public List<FinancePaymentItemPo> findByPayAmountBySettleOrderNos(List<String> financeSettleOrderNos) {
        if (CollectionUtils.isEmpty(financeSettleOrderNos)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<FinancePaymentItemPo>lambdaQuery()
                .select(FinancePaymentItemPo::getPaymentBizNo,
                        FinancePaymentItemPo::getRmbPaymentMoney)
                .eq(FinancePaymentItemPo::getPaymentBizType, PaymentBizType.SETTLEMENT)
                .in(CollectionUtils.isNotEmpty(financeSettleOrderNos),
                        FinancePaymentItemPo::getPaymentBizNo, financeSettleOrderNos));
    }
}
