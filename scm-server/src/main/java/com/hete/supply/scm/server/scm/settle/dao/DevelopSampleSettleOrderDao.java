package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleSettleOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 样品结算结算单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Component
@Validated
public class DevelopSampleSettleOrderDao extends BaseDao<DevelopSampleSettleOrderMapper, DevelopSampleSettleOrderPo> {

    public CommonPageResult.PageInfo<DevelopSampleSettleSearchVo> search(Page<Void> page, DevelopSampleSettleSearchDto dto) {
        IPage<DevelopSampleSettleSearchVo> pageResult = baseMapper.search(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public DevelopSampleSettleOrderPo getByDevelopSampleSettleOrderNo(String developSampleSettleOrderNo) {
        return baseMapper.selectOne(Wrappers.<DevelopSampleSettleOrderPo>lambdaQuery()
                .eq(DevelopSampleSettleOrderPo::getDevelopSampleSettleOrderNo, developSampleSettleOrderNo));
    }

    public Integer getExportTotals(DevelopSampleSettleSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<DevelopSampleSettleOrderExportVo> getExportList(Page<Void> page, DevelopSampleSettleSearchDto dto) {
        IPage<DevelopSampleSettleOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<DevelopSampleSettleOrderPo> getListBySupplierCodeAndMonth(String supplierCode, String month) {
        return list(Wrappers.<DevelopSampleSettleOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(supplierCode), DevelopSampleSettleOrderPo::getSupplierCode, supplierCode)
                .eq(StringUtils.isNotBlank(month), DevelopSampleSettleOrderPo::getMonth, month));
    }

    public Map<String, DevelopSampleSettleOrderPo> getMapByNoList(List<String> settleOrderNoList) {
        if (CollectionUtils.isEmpty(settleOrderNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<DevelopSampleSettleOrderPo>lambdaQuery()
                        .in(DevelopSampleSettleOrderPo::getDevelopSampleSettleOrderNo, settleOrderNoList))
                .stream()
                .collect(Collectors.toMap(DevelopSampleSettleOrderPo::getDevelopSampleSettleOrderNo, Function.identity()));
    }


}
