package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderItemPo;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 财务结算单明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceSettleOrderItemDao extends BaseDao<FinanceSettleOrderItemMapper, FinanceSettleOrderItemPo> {

    /**
     * 根据对账单号列表查询结算单信息。
     *
     * @param recoOrderNos 对账单号列表
     * @return 结算单信息列表
     */
    public List<FinanceSettleOrderItemPo> getSettlementOrdersByRecoOrderNos(Collection<String> recoOrderNos) {
        // 创建 LambdaQueryWrapper 构建查询条件
        LambdaQueryWrapper<FinanceSettleOrderItemPo> queryWrapper = new LambdaQueryWrapper<>();

        // 添加对账单号列表的查询条件
        queryWrapper.in(FinanceSettleOrderItemPo::getBusinessNo, recoOrderNos);
        queryWrapper.eq(FinanceSettleOrderItemPo::getFinanceSettleOrderItemType, FinanceSettleOrderItemType.RECO_ORDER);

        // 执行查询并返回结果
        return baseMapper.selectList(queryWrapper);
    }

    public List<FinanceSettleOrderItemPo> findBySettleOrderNo(String financeSettleOrderNo) {
        return lambdaQuery().eq(FinanceSettleOrderItemPo::getFinanceSettleOrderNo, financeSettleOrderNo)
                .list();
    }

    public List<FinanceSettleOrderItemPo> findByCarryoverOrderNo(String carryoverOrderNo) {
        return lambdaQuery().eq(FinanceSettleOrderItemPo::getBusinessNo, carryoverOrderNo)
                .eq(FinanceSettleOrderItemPo::getFinanceSettleOrderItemType,
                        FinanceSettleOrderItemType.CARRYOVER_ORDER)
                .list();
    }

    public IPage<FinanceSettleOrderItemPo> findSettleOrderItems(SearchSettleOrderDto dto) {
        Page<FinanceSettleOrderItemPo> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        return baseMapper.findPageSettleOrderItems(page, dto);
    }

    public Integer findExportSettleItemTotalCount(SearchSettleOrderDto dto) {
        return baseMapper.findExportSettleItemTotalCount(dto);
    }

    public List<String> findSettleOrderNosByRecoOrderNo(String recoOrderNo) {
        LambdaQueryWrapper<FinanceSettleOrderItemPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FinanceSettleOrderItemPo::getBusinessNo, recoOrderNo)
                .eq(FinanceSettleOrderItemPo::getFinanceSettleOrderItemType, FinanceSettleOrderItemType.RECO_ORDER)
                .select(FinanceSettleOrderItemPo::getFinanceSettleOrderNo);

        return baseMapper.selectList(queryWrapper)
                .stream()
                .distinct()
                .map(FinanceSettleOrderItemPo::getFinanceSettleOrderNo)
                .collect(Collectors.toList());
    }
}
