package com.hete.supply.scm.server.scm.process.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 返修单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-29
 */
@Component
@Validated
public class RepairOrderItemDao extends BaseDao<RepairOrderItemMapper, RepairOrderItemPo> {

    public RepairOrderItemPo getByBatchCode(String batchCode) {
        if (StrUtil.isBlank(batchCode)) {
            return null;
        }
        return baseMapper.selectOne(new LambdaQueryWrapper<RepairOrderItemPo>()
                .eq(RepairOrderItemPo::getBatchCode, batchCode));
    }

    public Map<String, List<RepairOrderItemPo>> getMapByRepairOrderNoList(List<String> repairOrderNoList) {
        if (CollectionUtils.isEmpty(repairOrderNoList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<RepairOrderItemPo>lambdaQuery()
                .in(RepairOrderItemPo::getRepairOrderNo, repairOrderNoList))
                .stream()
                .collect(Collectors.groupingBy(RepairOrderItemPo::getRepairOrderNo));
    }

    public List<RepairOrderItemPo> getListByRepairOrderNo(String repairOrderNo) {
        if (StrUtil.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<RepairOrderItemPo>lambdaQuery()
                .eq(RepairOrderItemPo::getRepairOrderNo, repairOrderNo));
    }

    public List<RepairOrderItemPo> listByRepairOrderNos(Collection<String> repairOrderNos) {
        return CollectionUtils.isEmpty(repairOrderNos) ?
                Collections.emptyList() :
                list(Wrappers.<RepairOrderItemPo>lambdaQuery()
                        .in(RepairOrderItemPo::getRepairOrderNo, repairOrderNos));
    }
}

