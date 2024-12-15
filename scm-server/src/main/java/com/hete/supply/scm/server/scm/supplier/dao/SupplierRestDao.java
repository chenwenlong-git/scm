package com.hete.supply.scm.server.scm.supplier.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ExportSupplierRestDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierRestExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierRestPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应商停工时间表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Component
@Validated
public class SupplierRestDao extends BaseDao<SupplierRestMapper, SupplierRestPo> {

    public List<SupplierRestPo> listBySupplierCode(String supplierCode) {
        if (StrUtil.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return lambdaQuery().eq(SupplierRestPo::getSupplierCode, supplierCode).list();
    }

    public List<SupplierRestPo> listBySupplierCodes(Collection<String> supplierCodes) {
        if (CollectionUtils.isEmpty(supplierCodes)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(SupplierRestPo::getSupplierCode, supplierCodes).list();
    }

    public int getExportTotals(ExportSupplierRestDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public IPage<SupplierRestExportVo> getByPage(ExportSupplierRestDto dto) {
        Page<SupplierRestExportVo> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        return baseMapper.getByPage(page, dto);
    }
}
