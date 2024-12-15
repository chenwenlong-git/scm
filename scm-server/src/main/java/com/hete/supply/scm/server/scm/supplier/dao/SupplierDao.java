package com.hete.supply.scm.server.scm.supplier.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SupplierSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.supply.scm.api.scm.entity.vo.SupplierExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierLogisticsBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierQuickSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierQuickSearchVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 供应商信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Component
@Validated
public class SupplierDao extends BaseDao<SupplierMapper, SupplierPo> {


    /**
     * 通过供应商代码模糊查询
     *
     * @author ChenWenLong
     * @date 2022/11/21 18:01
     */
    public List<SupplierPo> getByLikeSupplierCode(String supplierCode) {
        return list(Wrappers.<SupplierPo>lambdaQuery().like(supplierCode != null, SupplierPo::getSupplierCode, supplierCode));
    }

    /**
     * 通过供应商代码精确查询
     *
     * @author ChenWenLong
     * @date 2022/11/21 18:01
     */
    public SupplierPo getBySupplierCode(String supplierCode) {
        if (StringUtils.isBlank(supplierCode)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierCode, supplierCode));
    }

    /**
     * 通过供应商代码精确查询
     *
     * @author ChenWenLong
     * @date 2022/11/21 18:01
     */
    public SupplierPo getBySupplierCodeAndVersion(String supplierCode, Integer version) {
        return baseMapper.selectOne(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierCode, supplierCode)
                .eq(SupplierPo::getVersion, version));
    }

    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/26 15:34
     */
    public CommonPageResult.PageInfo<SupplierVo> selectSupplierPage(Page<Void> page, SupplierDto dto) {
        IPage<SupplierVo> pageResult = baseMapper.selectSupplierPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 下拉列表
     *
     * @author ChenWenLong
     * @date 2022/11/26 15:34
     */
    public CommonPageResult.PageInfo<SupplierQuickSearchVo> getSupplierQuickSearch(Page<Void> page, SupplierQuickSearchDto dto) {
        IPage<SupplierQuickSearchVo> pageResult = baseMapper.getSupplierQuickSearch(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过供应商代码批量查询
     *
     * @author ChenWenLong
     * @date 2022/11/29 10:15
     */
    public List<SupplierPo> getBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPo>lambdaQuery()
                .in(SupplierPo::getSupplierCode, supplierCodeList));
    }

    public List<SupplierPo> getAllSupplierList() {
        return baseMapper.selectList(null);
    }

    public Map<String, SupplierPo> getSupplierMapBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                        .in(SupplierPo::getSupplierCode, supplierCodeList))
                .stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, Function.identity()));
    }

    public List<SupplierPo> getSupplierByName(String supplierName) {
        if (StringUtils.isBlank(supplierName)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierName, supplierName));
    }

    public List<SupplierPo> getSupplierByCodeOrName(String supplierCode, String supplierName) {
        if (StringUtils.isBlank(supplierCode) && StringUtils.isBlank(supplierName)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(supplierCode), SupplierPo::getSupplierCode, supplierCode).or()
                .eq(StringUtils.isNotBlank(supplierName), SupplierPo::getSupplierName, supplierName));
    }


    public List<SupplierPo> getSupplierByName(String supplierName, String notSupplierCode) {
        if (StringUtils.isBlank(notSupplierCode) && StringUtils.isBlank(supplierName)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierName, supplierName)
                .ne(SupplierPo::getSupplierCode, notSupplierCode));
    }

    /**
     * 统计导出的总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(SupplierSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<SupplierExportVo> getExportList(Page<Void> page, SupplierSearchDto dto) {
        IPage<SupplierExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 根据供应商名称查询供应商
     *
     * @param supplierName
     * @return
     */
    public SupplierPo getOneBySupplierName(String supplierName) {
        if (StringUtils.isBlank(supplierName)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierName, supplierName));
    }

    /**
     * 通过供应商代码和类型批量查询
     *
     * @author ChenWenLong
     * @date 2023/5/6 14:55
     */
    public List<SupplierPo> getBySupplierCodeListAndType(List<String> supplierCodeList, List<SupplierType> supplierTypeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPo>lambdaQuery()
                .in(SupplierPo::getSupplierCode, supplierCodeList)
                .in(CollectionUtils.isNotEmpty(supplierTypeList), SupplierPo::getSupplierType, supplierTypeList));
    }

    public Map<String, String> getSupplierNameBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                        .in(SupplierPo::getSupplierCode, supplierCodeList))
                .stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, SupplierPo::getSupplierName));
    }

    public List<SupplierPo> getByTypeList(List<SupplierType> supplierTypeList) {
        if (CollectionUtils.isEmpty(supplierTypeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPo>lambdaQuery()
                .in(SupplierPo::getSupplierType, supplierTypeList));
    }

    public List<SupplierPo> getListBySupplierGrade(SupplierGrade supplierGrade) {
        if (supplierGrade == null) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPo>lambdaQuery()
                .in(SupplierPo::getSupplierGrade, supplierGrade));
    }

    public Map<String, String> getMapAliasBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                        .in(SupplierPo::getSupplierCode, supplierCodeList))
                .stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, SupplierPo::getSupplierAlias));
    }

    public List<String> findSupplierCodesByFollower(String followUser) {
        if (StrUtil.isBlank(followUser)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<SupplierPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SupplierPo::getSupplierCode).eq(SupplierPo::getFollowUser, followUser);

        return baseMapper.selectList(queryWrapper)
                .stream()
                .map(SupplierPo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
    }

    public Set<String> findSupplierCodesBySettleTime(String settleTime) {
        // 构建查询条件
        LambdaQueryWrapper<SupplierPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SupplierPo::getSupplierCode);
        queryWrapper.eq(SupplierPo::getSettleTime, settleTime);

        // 查询供应商列表
        List<SupplierPo> suppliers = baseMapper.selectList(queryWrapper);

        // 提取供应商编码
        return suppliers.stream()
                .map(SupplierPo::getSupplierCode)
                .collect(Collectors.toSet());
    }

    public List<SupplierPo> getListByStatusAndReconciliationCycle(SupplierStatus supplierStatus,
                                                                  List<ReconciliationCycle> reconciliationCycleList) {
        if (null == supplierStatus) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierStatus, supplierStatus)
                .in(CollectionUtils.isNotEmpty(reconciliationCycleList), SupplierPo::getReconciliationCycle, reconciliationCycleList));
    }

    public List<SupplierPo> getSupplierPoListByFollowUser(String followUser) {
        if (StringUtils.isBlank(followUser)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getFollowUser, followUser));
    }

    public SupplierPo getBySupplierAlias(String supplierAlias) {
        if (StringUtils.isBlank(supplierAlias)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierAlias, supplierAlias));
    }

    public List<SupplierPo> getSupplierByAliasNotCode(String supplierAlias, String notSupplierCode) {
        if (StringUtils.isBlank(notSupplierCode) && StringUtils.isBlank(supplierAlias)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .eq(SupplierPo::getSupplierAlias, supplierAlias)
                .ne(SupplierPo::getSupplierCode, notSupplierCode));
    }

    public List<SupplierPo> getListBySupplierCodeListAndStatus(List<String> supplierCodeList,
                                                               SupplierStatus supplierStatus) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .in(SupplierPo::getSupplierCode, supplierCodeList)
                .eq(SupplierPo::getSupplierStatus, supplierStatus));
    }

    public List<SupplierLogisticsBo> listByLogisticsBo(Collection<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.listByLogisticsBo(supplierCodeList);
    }

    public List<SupplierPo> getListByNameAndStatus(String supplierCode, SupplierStatus supplierStatus) {
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPo>lambdaQuery()
                .like(SupplierPo::getSupplierCode, supplierCode)
                .eq(null != supplierStatus, SupplierPo::getSupplierStatus, supplierStatus));
    }
}
