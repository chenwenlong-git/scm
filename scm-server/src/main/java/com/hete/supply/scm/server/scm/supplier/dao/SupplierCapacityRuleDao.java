package com.hete.supply.scm.server.scm.supplier.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapRuleBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityRulePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * <p>
 * 供应商产能规则表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Component
@Validated
public class SupplierCapacityRuleDao extends BaseDao<SupplierCapacityRuleMapper, SupplierCapacityRulePo> {

    public SupplierCapacityRulePo getBySupplierCode(String supplierCode) {
        if (StrUtil.isBlank(supplierCode)) {
            return null;
        }
        return lambdaQuery().eq(SupplierCapacityRulePo::getSupplierCode, supplierCode).one();
    }

    public Set<String> getExistRuleSupplierCodes() {
        return lambdaQuery().select(SupplierCapacityRulePo::getSupplierCode).list().stream().map(SupplierCapacityRulePo::getSupplierCode).collect(toSet());
    }

    public CommonPageResult.PageInfo<SupplierCapacityPageVo> page(SupCapacityPageDto dto) {
        IPage<SupplierCapacityPageVo> pageResult = baseMapper.page(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }


    public int getSupCapacityRuleExportTotal(SupCapacityPageDto dto) {
        return baseMapper.getSupCapacityRuleExportTotal(dto);
    }

    public List<SupCapRuleBo> listBySupplierCodes(Collection<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.listBySupplierCodes(supplierCodeList);
    }
}
