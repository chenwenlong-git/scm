package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 供应商产品对照表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Component
@Validated
public class SupplierProductCompareDao extends BaseDao<SupplierProductCompareMapper, SupplierProductComparePo> {

    /**
     * 通过批量SKU查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public Map<String, List<SupplierProductComparePo>> getBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSku, skuList).orderByDesc(SupplierProductComparePo::getCreateTime)).stream().collect(Collectors.groupingBy(SupplierProductComparePo::getSku));
    }

    /**
     * 通过单个SKU查询
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:33
     */
    public List<SupplierProductComparePo> getBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .eq(SupplierProductComparePo::getSku, sku)
                .orderByDesc(SupplierProductComparePo::getSupplierProductCompareId));
    }


    /**
     * 批量删除
     *
     * @author ChenWenLong
     * @date 2022/11/26 18:22
     */
    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    /**
     * 通过sku批量查询含sku和供应商作为key的map集合
     *
     * @author ChenWenLong
     * @date 2023/3/29 14:19
     */
    public Map<String, SupplierProductComparePo> getBatchSkuMap(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }

        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSku, skuList)).stream()
                .collect(Collectors.toMap(supplierProductComparePo ->
                        supplierProductComparePo.getSupplierCode() + supplierProductComparePo.getSku(), Function.identity()));
    }


    /**
     * 通过供应商代码批量查询
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:33
     */
    public List<SupplierProductComparePo> getBatchSupplierCode(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSupplierCode, supplierCodeList));
    }

    /**
     * 通过供应商产品名称批量查询
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:33
     */
    public List<SupplierProductComparePo> getBatchSupplierProductName(List<String> supplierProductNameList) {
        if (CollectionUtils.isEmpty(supplierProductNameList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSupplierProductName, supplierProductNameList));
    }

    /**
     * 通过供应商和sku查询是否有重复
     *
     * @author ChenWenLong
     * @date 2023/3/30 13:51
     */
    public SupplierProductComparePo getBySupplierCodeAndSku(@NotBlank String supplierCode, @NotBlank String sku) {
        return baseMapper.selectOne(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .eq(SupplierProductComparePo::getSupplierCode, supplierCode)
                .eq(SupplierProductComparePo::getSku, sku));
    }

    /**
     * 通过供应商产品名称或供应商代码批量查询
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:33
     */
    public List<SupplierProductComparePo> getBatchProductNameOrSupplierCode(List<String> supplierProductNameList,
                                                                            List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierProductNameList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSupplierProductName, supplierProductNameList)
                .in(CollectionUtils.isNotEmpty(supplierCodeList), SupplierProductComparePo::getSupplierCode, supplierCodeList));
    }

    /**
     * 查询指定供应商产品名称
     *
     * @param skuList:
     * @param supplierCode:
     * @return Map<String, String>
     * @author ChenWenLong
     * @date 2023/7/1 16:04
     */
    public Map<String, String> getBatchSkuMapAndSupplierCode(List<String> skuList, String supplierCode) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }

        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSku, skuList)
                .eq(StringUtils.isNotBlank(supplierCode), SupplierProductComparePo::getSupplierCode, supplierCode))
                .stream()
                .collect(Collectors.toMap(SupplierProductComparePo::getSku, SupplierProductComparePo::getSupplierProductName));
    }

    /**
     * 通过供应商产品名称模糊查询
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:33
     */
    public List<SupplierProductComparePo> getByLikeSupplierProductName(String supplierProductName) {
        if (StringUtils.isBlank(supplierProductName)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .like(SupplierProductComparePo::getSupplierProductName, supplierProductName));
    }

    public List<SupplierProductComparePo> getListBySupplierProductName(String supplierProductName) {
        if (StringUtils.isBlank(supplierProductName)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .eq(SupplierProductComparePo::getSupplierProductName, supplierProductName));
    }

    /**
     * 通过供应商产品名称模糊查询或供应商代码
     *
     * @param supplierProductName:
     * @param supplierCodeList:
     * @return List<SupplierProductComparePo>
     * @author ChenWenLong
     * @date 2023/7/13 17:23
     */
    public List<SupplierProductComparePo> getByLikeProductNameOrSupplierCode(String supplierProductName,
                                                                             List<String> supplierCodeList) {
        if (StringUtils.isBlank(supplierProductName)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .like(SupplierProductComparePo::getSupplierProductName, supplierProductName)
                .in(CollectionUtils.isNotEmpty(supplierCodeList), SupplierProductComparePo::getSupplierCode, supplierCodeList));
    }

    public Long getTotalsCnt() {
        return baseMapper.selectCount(null);
    }

    public IPage<SupplierProductComparePo> getByPage(Page<SupplierProductComparePo> page) {
        return baseMapper.selectPage(page, null);
    }

    public List<SupplierProductComparePo> getListByBatchSku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSku, skuList));
    }

    public Map<String, String> getBatchSupplierMapAndSku(List<String> supplierCodeList, String sku) {
        if (CollectionUtils.isEmpty(supplierCodeList) || StringUtils.isBlank(sku)) {
            return Collections.emptyMap();
        }

        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSupplierCode, supplierCodeList)
                .eq(StringUtils.isNotBlank(sku), SupplierProductComparePo::getSku, sku))
                .stream()
                .collect(Collectors.toMap(SupplierProductComparePo::getSupplierCode, SupplierProductComparePo::getSupplierProductName));
    }

    /**
     * 通过sku和状态查询
     *
     * @param sku:
     * @param supplierProductCompareStatus:
     * @return List<SupplierProductComparePo>
     * @author ChenWenLong
     * @date 2024/10/9 10:08
     */
    public List<SupplierProductComparePo> getBySkuAndStatus(String sku, BooleanType supplierProductCompareStatus) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .eq(SupplierProductComparePo::getSku, sku)
                .eq(supplierProductCompareStatus != null, SupplierProductComparePo::getSupplierProductCompareStatus, supplierProductCompareStatus));
    }

    public List<SupplierProductComparePo> getBySkuListAndStatus(List<String> skuList, BooleanType supplierProductCompareStatus) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSku, skuList)
                .eq(supplierProductCompareStatus != null, SupplierProductComparePo::getSupplierProductCompareStatus, supplierProductCompareStatus));
    }


    public IPage<SupplierProductComparePo> getInitData(Page<SupplierProductComparePo> page) {
        return baseMapper.getInitData(page);
    }

    public List<SupplierProductComparePo> listBySupCodeListAndSkuList(List<String> supCodeList, List<String> skuList) {
        return list(Wrappers.<SupplierProductComparePo>lambdaQuery()
                .in(SupplierProductComparePo::getSupplierCode, supCodeList)
                .in(SupplierProductComparePo::getSku, skuList));
    }
}
