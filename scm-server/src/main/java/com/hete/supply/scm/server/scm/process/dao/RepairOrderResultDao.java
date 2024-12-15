package com.hete.supply.scm.server.scm.process.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderResultPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 返修单结果表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-04
 */
@Component
@Validated
public class RepairOrderResultDao extends BaseDao<RepairOrderResultMapper, RepairOrderResultPo> {

    public List<RepairOrderResultPo> listByRepairOrderItemId(Long repairOrderItemId) {
        if (Objects.isNull(repairOrderItemId)) {
            return null;
        }
        return baseMapper.selectList(new LambdaQueryWrapper<RepairOrderResultPo>().
                eq(RepairOrderResultPo::getRepairOrderItemId, repairOrderItemId));
    }

    public List<RepairOrderResultPo> getListByRepairOrderNo(String repairOrderNo) {
        if (StrUtil.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<RepairOrderResultPo>lambdaQuery()
                .eq(RepairOrderResultPo::getRepairOrderNo, repairOrderNo));
    }

    public List<RepairOrderResultPo> listByRepairOrderNos(Collection<String> repairOrderNos) {
        return CollectionUtils.isEmpty(repairOrderNos) ?
                Collections.emptyList() :
                list(Wrappers.<RepairOrderResultPo>lambdaQuery()
                        .in(RepairOrderResultPo::getRepairOrderNo, repairOrderNos));
    }

    public List<RepairOrderResultPo> listByRepairOrderNo(String repairOrderNo) {
        return StrUtil.isBlank(repairOrderNo) ?
                Collections.emptyList() :
                list(Wrappers.<RepairOrderResultPo>lambdaQuery()
                        .eq(RepairOrderResultPo::getRepairOrderNo, repairOrderNo));
    }

    public List<RepairOrderResultPo> getListByRepairOrderNoList(List<String> repairOrderNoList) {
        if (CollectionUtils.isEmpty(repairOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<RepairOrderResultPo>lambdaQuery()
                .in(RepairOrderResultPo::getRepairOrderNo, repairOrderNoList));
    }
}
