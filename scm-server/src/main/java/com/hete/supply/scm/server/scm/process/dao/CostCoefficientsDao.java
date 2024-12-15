package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.CostCoefficientsRequestDto;
import com.hete.supply.scm.server.scm.process.converter.CostCoefficientsConverter;
import com.hete.supply.scm.server.scm.process.entity.po.CostCoefficientsPo;
import com.hete.supply.scm.server.scm.process.entity.vo.CostCoefficientsVo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 固定成本系数 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-20
 */
@Component
@Validated
public class CostCoefficientsDao extends BaseDao<CostCoefficientsMapper, CostCoefficientsPo> {

    public IPage<CostCoefficientsVo> getByPage(CostCoefficientsRequestDto dto,
                                               Page<CostCoefficientsPo> page) {
        LambdaQueryWrapper<CostCoefficientsPo> pageWrapper = Wrappers.<CostCoefficientsPo>lambdaQuery()
                .ge(Objects.nonNull(dto.getEffectiveTimeStart()), CostCoefficientsPo::getEffectiveTime,
                        dto.getEffectiveTimeStart())
                .le(Objects.nonNull(dto.getEffectiveTimeEnd()), CostCoefficientsPo::getEffectiveTime,
                        dto.getEffectiveTimeEnd())
                .orderByDesc(CostCoefficientsPo::getEffectiveTime);
        Page<CostCoefficientsPo> costCoefficientsPoPage = baseMapper.selectPage(page, pageWrapper);
        return costCoefficientsPoPage.convert(CostCoefficientsConverter::convertToVo);
    }

    public List<CostCoefficientsPo> listByEffectiveTimes(List<LocalDateTime> updateEffectiveTimes) {
        return CollectionUtils.isEmpty(updateEffectiveTimes) ?
                Collections.emptyList() :
                list(Wrappers.<CostCoefficientsPo>lambdaQuery()
                        .in(CostCoefficientsPo::getEffectiveTime, updateEffectiveTimes));
    }

    public List<CostCoefficientsPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }
}
