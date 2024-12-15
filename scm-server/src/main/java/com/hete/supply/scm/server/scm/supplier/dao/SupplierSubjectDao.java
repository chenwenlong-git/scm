package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierSubjectPo;
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
 * 供应商主体信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class SupplierSubjectDao extends BaseDao<SupplierSubjectMapper, SupplierSubjectPo> {

    public List<SupplierSubjectPo> getListBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierSubjectPo>lambdaQuery()
                .in(SupplierSubjectPo::getSupplierCode, supplierCodeList));
    }

    public SupplierSubjectPo getBySubject(String subject) {
        if (StringUtils.isBlank(subject)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SupplierSubjectPo>lambdaQuery()
                .eq(SupplierSubjectPo::getSubject, subject));
    }

    public Map<String, SupplierSubjectPo> getMapBySubjectList(List<String> subjectList) {
        if (CollectionUtils.isEmpty(subjectList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<SupplierSubjectPo>lambdaQuery()
                .in(SupplierSubjectPo::getSubject, subjectList)).stream()
                .collect(Collectors.toMap(SupplierSubjectPo::getSubject, supplierSubjectPo -> supplierSubjectPo));
    }

    public List<SupplierSubjectPo> getListBySupplierCode(String supplierCode) {
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierSubjectPo>lambdaQuery()
                .eq(SupplierSubjectPo::getSupplierCode, supplierCode));
    }

    public List<SupplierSubjectPo> getListBySubjectList(List<String> subjectList) {
        if (CollectionUtils.isEmpty(subjectList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierSubjectPo>lambdaQuery()
                .in(SupplierSubjectPo::getSubject, subjectList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return super.removeBatchByIds(list);
    }

}
