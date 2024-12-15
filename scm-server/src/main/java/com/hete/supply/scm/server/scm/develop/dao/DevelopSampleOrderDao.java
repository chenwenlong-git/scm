package com.hete.supply.scm.server.scm.develop.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleType;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 开发需求样品单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-03
 */
@Component
@Validated
public class DevelopSampleOrderDao extends BaseDao<DevelopSampleOrderMapper, DevelopSampleOrderPo> {

    public Map<String, DevelopSampleOrderPo> getMapByNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                        .in(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList))
                .stream()
                .collect(Collectors.toMap(DevelopSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));
    }

    public List<DevelopSampleOrderPo> getListByLikeSku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .like(DevelopSampleOrderPo::getSku, sku));
    }

    public List<DevelopSampleOrderPo> getListBySku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getSku, skuList));
    }

    public List<DevelopSampleOrderPo> getListByLikeSkuBatchCode(String skuBatchCode) {
        if (StringUtils.isBlank(skuBatchCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .like(DevelopSampleOrderPo::getSkuBatchCode, skuBatchCode));
    }

    public void updateStatusBatchNoList(List<String> developSampleOrderNoList, DevelopSampleStatus developSampleStatus) {
        if (CollectionUtil.isNotEmpty(developSampleOrderNoList)) {
            final DevelopSampleOrderPo developSampleOrderPo = new DevelopSampleOrderPo();
            developSampleOrderPo.setDevelopSampleStatus(developSampleStatus);
            baseMapper.update(developSampleOrderPo, Wrappers.<DevelopSampleOrderPo>lambdaUpdate()
                    .in(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList));
        }
    }

    public List<DevelopSampleOrderPo> getListByStatusList(@NotEmpty List<DevelopSampleStatus> developSampleStatusList,
                                                          List<String> supplierCodeList,
                                                          LocalDateTime warehousingTimeStart,
                                                          LocalDateTime warehousingTimeEnd,
                                                          List<DevelopSampleMethod> developSampleMethodList,
                                                          List<String> notInSupplierCodeList) {
        if (CollectionUtils.isEmpty(developSampleStatusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopSampleStatus, developSampleStatusList)
                .in(CollectionUtils.isNotEmpty(supplierCodeList), DevelopSampleOrderPo::getSupplierCode, supplierCodeList)
                .ge(null != warehousingTimeStart, DevelopSampleOrderPo::getShelvesTime, warehousingTimeStart)
                .le(null != warehousingTimeEnd, DevelopSampleOrderPo::getShelvesTime, warehousingTimeEnd)
                .in(CollectionUtils.isNotEmpty(developSampleMethodList), DevelopSampleOrderPo::getDevelopSampleMethod, developSampleMethodList)
                .notIn(CollectionUtils.isNotEmpty(notInSupplierCodeList), DevelopSampleOrderPo::getSupplierCode, notInSupplierCodeList));
    }

    public List<DevelopSampleOrderPo> getListByNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public CommonPageResult.PageInfo<DevelopSampleOrderSearchVo> search(Page<Void> page, DevelopSampleOrderSearchDto dto) {
        IPage<DevelopSampleOrderSearchVo> pageResult = baseMapper.search(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getExportTotals(DevelopSampleOrderSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<DevelopSampleOrderExportVo> getExportList(Page<Void> page, DevelopSampleOrderSearchDto dto) {
        IPage<DevelopSampleOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public DevelopSampleOrderPo getOneByNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNo));

    }

    public List<DevelopSampleOrderPo> getListByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopChildOrderNo, developChildOrderNoList));
    }

    public List<DevelopSampleOrderPo> getListByDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopChildOrderNo, developChildOrderNo));
    }

    public List<DevelopSampleOrderPo> getListByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public List<DevelopSampleOrderPo> getListByDevelopPricingOrderNoList(List<String> developPricingOrderNoList) {
        if (CollectionUtils.isEmpty(developPricingOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopPricingOrderNo, developPricingOrderNoList));
    }

    public List<DevelopSampleOrderPo> getListByDevelopPricingOrderNo(String developPricingOrderNo) {
        if (StringUtils.isBlank(developPricingOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopPricingOrderNo, developPricingOrderNo));
    }

    public List<DevelopSampleOrderPo> getListByDevelopPamphletOrderNoList(List<String> developPamphletOrderNoList) {
        if (CollectionUtils.isEmpty(developPamphletOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNoList));
    }

    public void updateDevelopSampleMethodBatchNoList(@NotEmpty List<String> developSampleOrderNoList,
                                                     DevelopSampleStatus developSampleStatus,
                                                     @NotNull DevelopSampleMethod developSampleMethod) {
        if (CollectionUtil.isNotEmpty(developSampleOrderNoList)) {
            final DevelopSampleOrderPo developSampleOrderPo = new DevelopSampleOrderPo();
            if (developSampleStatus != null) {
                developSampleOrderPo.setDevelopSampleStatus(developSampleStatus);
            }
            if (developSampleMethod != null) {
                developSampleOrderPo.setDevelopSampleMethod(developSampleMethod);
            }
            baseMapper.update(developSampleOrderPo, Wrappers.<DevelopSampleOrderPo>lambdaUpdate()
                    .in(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList));
        }
    }

    public List<DevelopSampleOrderPo> getListByNoAndMethod(String developPamphletOrderNo, DevelopSampleMethod developSampleMethod) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo)
                .eq(DevelopSampleOrderPo::getDevelopSampleMethod, developSampleMethod));
    }

    public List<DevelopSampleOrderPo> getListByChildNoAndMethodAndType(String developChildOrderNo,
                                                                       DevelopSampleMethod developSampleMethod,
                                                                       DevelopSampleType type) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopChildOrderNo, developChildOrderNo)
                .eq(DevelopSampleOrderPo::getDevelopSampleMethod, developSampleMethod)
                .eq(type != null, DevelopSampleOrderPo::getDevelopSampleType, type));
    }

    public List<DevelopSampleOrderPo> getListByLikeSkuBatchCodeList(List<String> skuBatchCodeList) {
        if (CollectionUtil.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getSkuBatchCode, skuBatchCodeList));
    }

    public List<DevelopSampleOrderPo> getListByChildNoListOrType(List<String> developChildOrderNoList, DevelopSampleType type) {
        if (CollectionUtil.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopChildOrderNo, developChildOrderNoList)
                .eq(type != null, DevelopSampleOrderPo::getDevelopSampleType, type));
    }

    public List<DevelopSampleOrderPo> getPreSampleOrderByNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(DevelopSampleOrderPo::getDevelopSampleType, DevelopSampleType.PRENATAL_SAMPLE)
                .eq(DevelopSampleOrderPo::getDevelopSampleStatus, DevelopSampleStatus.DOCUMENTARY_RECEIPT)
                .like(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNo));
    }

    public List<DevelopSampleOrderPo> getListByChildNoOrSampleNoList(String developPamphletOrderNo,
                                                                     List<String> developSampleOrderNoList) {
        if (StringUtils.isBlank(developPamphletOrderNo)
                && CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(developPamphletOrderNo), DevelopSampleOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo)
                .in(CollectionUtils.isNotEmpty(developSampleOrderNoList), DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public Map<String, DevelopSampleOrderPo> getMapByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                        .in(DevelopSampleOrderPo::getDevelopChildOrderNo, developChildOrderNoList))
                .stream().collect(Collectors.toMap(DevelopSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));
    }

    public List<DevelopSampleOrderPo> getListByInitSkuBatchSamplePrice() {
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .ne(DevelopSampleOrderPo::getSkuBatchCode, ""));
    }

    public List<DevelopSampleOrderPo> getListBySkuBatchCodeList(List<String> skuBatchCodeList) {
        if (CollectionUtils.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getSkuBatchCode, skuBatchCodeList));
    }

    public List<DevelopSampleOrderPo> getListBySkuOrderByHandleTime(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getSku, skuList)
                .orderByDesc(DevelopSampleOrderPo::getHandleTime));
    }

    public List<DevelopSampleOrderPo> getListByLikeNo(String developSampleOrderNo,
                                                      String supplierCode,
                                                      List<DevelopSampleStatus> statusList) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .like(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNo)
                .eq(StringUtils.isNotBlank(supplierCode), DevelopSampleOrderPo::getSupplierCode, supplierCode)
                .in(CollectionUtil.isNotEmpty(statusList), DevelopSampleOrderPo::getDevelopSampleStatus, statusList));
    }

    public List<DevelopSampleOrderPo> getListByNoListAndSupplierCode(List<String> developSampleOrderNoList,
                                                                     String supplierCode,
                                                                     List<DevelopSampleStatus> statusList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList)
                .eq(StringUtils.isNotBlank(supplierCode), DevelopSampleOrderPo::getSupplierCode, supplierCode)
                .in(CollectionUtil.isNotEmpty(statusList), DevelopSampleOrderPo::getDevelopSampleStatus, statusList));
    }

    public List<DevelopSampleOrderPo> getListByChildOrderNoListAndType(List<String> developChildOrderNoList,
                                                                       DevelopSampleMethod developSampleMethod,
                                                                       List<DevelopSampleType> developSampleTypeList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderPo>lambdaQuery()
                .in(DevelopSampleOrderPo::getDevelopChildOrderNo, developChildOrderNoList)
                .eq(null != developSampleMethod, DevelopSampleOrderPo::getDevelopSampleMethod, developSampleMethod)
                .in(CollectionUtils.isNotEmpty(developSampleTypeList), DevelopSampleOrderPo::getDevelopSampleType, developSampleTypeList));
    }


    public List<DevelopSampleOrderPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }
}
