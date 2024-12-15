package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.DevelopChildOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopChildGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopChildSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
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
 * 开发子单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Component
@Validated
public class DevelopChildOrderDao extends BaseDao<DevelopChildOrderMapper, DevelopChildOrderPo> {

    public List<DevelopChildOrderPo> getListByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .in(DevelopChildOrderPo::getDevelopChildOrderNo, developChildOrderNoList));
    }

    public CommonPageResult.PageInfo<DevelopChildSearchVo> searchDevelopChild(Page<Void> page, DevelopChildSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.searchDevelopChild(page, dto));

    }

    public DevelopChildOrderPo getOneByDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .eq(DevelopChildOrderPo::getDevelopChildOrderNo, developChildOrderNo));
    }

    public List<DevelopChildOrderPo> getListByDevelopParentOrderNo(String developParentOrderNo) {
        if (StringUtils.isBlank(developParentOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .eq(DevelopChildOrderPo::getDevelopParentOrderNo, developParentOrderNo));
    }

    public Integer getExportTotals(DevelopChildSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<DevelopChildOrderExportVo> getExportList(Page<Void> page, DevelopChildSearchDto dto) {
        IPage<DevelopChildOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<DevelopChildGroupByStatusBo> getListByGroupByStatus(List<String> supplierCodeList) {
        return baseMapper.getListByGroupByStatus(supplierCodeList);
    }

    public List<DevelopChildOrderPo> getListByStatusList(List<DevelopChildOrderStatus> statusList) {
        if (CollectionUtils.isEmpty(statusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .in(DevelopChildOrderPo::getDevelopChildOrderStatus, statusList));
    }

    public List<DevelopChildOrderPo> getListByParentOrderNoAndNotStatus(String developParentOrderNo,
                                                                        List<DevelopChildOrderStatus> notStatusList) {
        if (StringUtils.isBlank(developParentOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .eq(DevelopChildOrderPo::getDevelopParentOrderNo, developParentOrderNo)
                .notIn(DevelopChildOrderPo::getDevelopChildOrderStatus, notStatusList));
    }

    public List<DevelopChildOrderPo> getListByParentOrderNoAndStatus(List<String> developChildOrderNoList,
                                                                     List<DevelopChildOrderStatus> statusList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .in(DevelopChildOrderPo::getDevelopChildOrderNo, developChildOrderNoList)
                .in(DevelopChildOrderPo::getDevelopChildOrderStatus, statusList));
    }

    public Map<String, DevelopChildOrderPo> getMapByNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                        .in(DevelopChildOrderPo::getDevelopChildOrderNo, developChildOrderNoList))
                .stream()
                .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));
    }

    public List<DevelopChildOrderPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }

    public List<DevelopChildOrderPo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderPo>lambdaQuery()
                .eq(DevelopChildOrderPo::getSku, sku));
    }
}
