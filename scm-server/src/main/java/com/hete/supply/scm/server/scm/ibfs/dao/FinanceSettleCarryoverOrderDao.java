package com.hete.supply.scm.server.scm.ibfs.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleCarryoverOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务结算结转单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceSettleCarryoverOrderDao extends BaseDao<FinanceSettleCarryoverOrderMapper,
        FinanceSettleCarryoverOrderPo> {

    public List<FinanceSettleCarryoverOrderPo> getCarryoverOrdersBySupplierAndAmount(String supplierCode,
                                                                                     BigDecimal minCarryoverAmount) {
        LambdaQueryWrapper<FinanceSettleCarryoverOrderPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FinanceSettleCarryoverOrderPo::getSupplierCode, supplierCode);
        queryWrapper.gt(FinanceSettleCarryoverOrderPo::getAvailableCarryoverAmount, minCarryoverAmount);
        queryWrapper.orderByDesc(FinanceSettleCarryoverOrderPo::getCreateTime);

        return baseMapper.selectList(queryWrapper);
    }

    public FinanceSettleCarryoverOrderPo findByCarryoverOrderNo(String carryoverOrderNo) {
        LambdaQueryWrapper<FinanceSettleCarryoverOrderPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FinanceSettleCarryoverOrderPo::getFinanceSettleCarryoverOrderNo, carryoverOrderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    public List<FinanceSettleCarryoverOrderPo> findByCarryoverOrderNos(Collection<String> carryoverOrderNos) {
        if (CollectionUtils.isEmpty(carryoverOrderNos)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<FinanceSettleCarryoverOrderPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FinanceSettleCarryoverOrderPo::getFinanceSettleCarryoverOrderNo, carryoverOrderNos);
        return baseMapper.selectList(queryWrapper);
    }

    public FinanceSettleCarryoverOrderPo findBySettleOrderNo(String settleOrderNo) {
        if (StrUtil.isBlank(settleOrderNo)) {
            return null;
        }

        LambdaQueryWrapper<FinanceSettleCarryoverOrderPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FinanceSettleCarryoverOrderPo::getFinanceSettleOrderNo, settleOrderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    public List<FinanceSettleCarryoverOrderPo> findBySettleOrderNos(List<String> settleOrderNos) {
        if (CollectionUtils.isEmpty(settleOrderNos)) {
            return null;
        }

        LambdaQueryWrapper<FinanceSettleCarryoverOrderPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FinanceSettleCarryoverOrderPo::getFinanceSettleOrderNo, settleOrderNos);
        return baseMapper.selectList(queryWrapper);
    }
}
