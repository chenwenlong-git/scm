package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.DevelopPamphletOrderStatus;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPamphletGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 开发版单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Component
@Validated
public class DevelopPamphletOrderDao extends BaseDao<DevelopPamphletOrderMapper, DevelopPamphletOrderPo> {

    public List<DevelopPamphletOrderPo> getListByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                .in(DevelopPamphletOrderPo::getDevelopChildOrderNo, developChildOrderNoList));
    }

    public List<DevelopPamphletOrderPo> getListByDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                .eq(DevelopPamphletOrderPo::getDevelopChildOrderNo, developChildOrderNo));
    }

    public DevelopPamphletOrderPo getByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                .eq(DevelopPamphletOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public DevelopPamphletOrderPo getByDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return null;
        }
        return getOne(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                .eq(DevelopPamphletOrderPo::getDevelopChildOrderNo, developChildOrderNo)
                .orderByDesc(DevelopPamphletOrderPo::getDevelopPamphletOrderId)
                .last("limit 1"));
    }

    public List<DevelopPamphletGroupByStatusBo> getListByGroupByStatus(List<String> supplierCodeList) {
        return baseMapper.getListByGroupByStatus(supplierCodeList);
    }

    public List<DevelopPamphletOrderPo> getListByStatusList(List<DevelopPamphletOrderStatus> statusList) {
        if (CollectionUtils.isEmpty(statusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                .in(DevelopPamphletOrderPo::getDevelopPamphletOrderStatus, statusList));
    }

    public List<DevelopPamphletOrderPo> getListByNoList(List<String> developPamphletOrderNoList) {
        if (CollectionUtils.isEmpty(developPamphletOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                .in(DevelopPamphletOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNoList));
    }

    public Map<String, DevelopPamphletOrderPo> getMapByNoList(List<String> developPamphletOrderNoList) {
        if (CollectionUtils.isEmpty(developPamphletOrderNoList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderPo>lambdaQuery()
                        .in(DevelopPamphletOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNoList))
                .stream()
                .collect(Collectors.toMap(DevelopPamphletOrderPo::getDevelopPamphletOrderNo, Function.identity()));
    }
}
