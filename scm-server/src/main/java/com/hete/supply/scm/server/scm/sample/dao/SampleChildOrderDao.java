package com.hete.supply.scm.server.scm.sample.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleListBySkuAndDevTypeVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderChangeSearchVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SamplePurchaseSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 样品需求子单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class SampleChildOrderDao extends BaseDao<SampleChildOrderMapper, SampleChildOrderPo> {

    public List<SampleChildOrderPo> getChildOrderByParentNo(String sampleParentOrderNo) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSampleParentOrderNo, sampleParentOrderNo));
    }

    public List<SampleChildOrderPo> getChildOrderByParentNoAndStatus(String sampleParentOrderNo,
                                                                     List<SampleOrderStatus> sampleOrderStatusList,
                                                                     List<String> authSupplierCode) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSampleParentOrderNo, sampleParentOrderNo)
                .in(CollectionUtils.isNotEmpty(sampleOrderStatusList),
                        SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusList)
                .in(CollectionUtils.isNotEmpty(sampleOrderStatusList),
                        SampleChildOrderPo::getSupplierCode, authSupplierCode));
    }


    public SampleChildOrderPo getLatestChildOrderPo(String sampleParentOrderNo) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return null;
        }

        return baseMapper.selectOneIgnoreDelete(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSampleParentOrderNo, sampleParentOrderNo)
                .orderByDesc(SampleChildOrderPo::getSampleChildOrderNo)
                .orderByDesc(SampleChildOrderPo::getSampleChildOrderId)
                .last("limit 1"));
    }

    public List<SampleChildOrderPo> getByIdList(Set<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectBatchIds(idList);
    }

    public void batchDeleteByIdList(Set<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        baseMapper.deleteBatchIds(idList);
    }


    public SampleChildOrderPo getOneByChildOrderNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public List<SampleChildOrderPo> searchSampleOrderChildNo(String sampleChildOrderNo) {
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .likeRight(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    /**
     * 通过模糊样品采购子单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/9 10:11
     */
    public List<SampleChildOrderPo> getListLikeBySampleChildOrderNo(String sampleChildOrderNo,
                                                                    String supplierCode,
                                                                    List<SampleOrderStatus> sampleOrderStatusNotList) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .like(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNo)
                .eq(StringUtils.isNotBlank(supplierCode), SampleChildOrderPo::getSupplierCode, supplierCode)
                .notIn(CollectionUtil.isNotEmpty(sampleOrderStatusNotList), SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusNotList));
    }

    public CommonPageResult.PageInfo<SamplePurchaseSearchVo> searchSamplePurchase(Page<Void> page, SamplePurchaseSearchDto dto) {
        final IPage<SamplePurchaseSearchVo> pageResult = baseMapper.searchSamplePurchase(page, dto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<SampleChildOrderPo> getListByChildNoList(List<String> sampleParentNoList) {
        if (CollectionUtils.isEmpty(sampleParentNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSampleParentOrderNo, sampleParentNoList));
    }

    public List<SampleChildOrderPo> getChildOrderListByChildNoList(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNoList)
                .orderByDesc(SampleChildOrderPo::getCreateTime));
    }

    public List<SampleChildOrderPo> getListBySku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSku, skuList));
    }

    /**
     * 通过供应商和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<SampleChildOrderChangeSearchVo> sampleChildOrderChangeSearch(String supplierCode, LocalDateTime sampleTime, LocalDateTime sampleTimeStart, LocalDateTime sampleTimeEnd, SampleOrderStatus sampleOrderStatus) {
        return baseMapper.sampleChildOrderChangeSearch(supplierCode, sampleTime, sampleTimeStart, sampleTimeEnd, sampleOrderStatus);
    }

    public Map<String, List<SampleChildOrderPo>> getMapByParentNoList(List<String> sampleParentOrderNoList) {
        if (CollectionUtils.isEmpty(sampleParentOrderNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                        .in(SampleChildOrderPo::getSampleParentOrderNo, sampleParentOrderNoList))
                .stream()
                .collect(Collectors.groupingBy(SampleChildOrderPo::getSampleParentOrderNo));
    }


    public List<String> getAllSkuList() {
        return baseMapper.getAllSkuList();
    }

    public void removeByParentOrderNo(@NotBlank String sampleParentOrderNo) {
        baseMapper.deleteSkipCheck(Wrappers.<SampleChildOrderPo>lambdaUpdate()
                .eq(SampleChildOrderPo::getSampleParentOrderNo, sampleParentOrderNo));
    }

    public Set<String> getDistinctNoListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptySet();
        }
        return baseMapper.getDistinctNoListBySkuList(skuList);
    }


    public SampleChildOrderPo getOneBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }

        return baseMapper.getOneBySku(sku);
    }

    public Integer getSampleChildExportTotals(SamplePurchaseSearchDto dto) {
        return baseMapper.getSampleChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<SampleChildExportVo> getSampleChildExportList(Page<Void> page, SamplePurchaseSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getSampleChildExportList(page, dto));
    }

    public List<String> getDefectiveSampleByNo(String sampleChildOrderNo, SampleOrderStatus sampleOrderStatus,
                                               SampleResult sampleResult) {
        return baseMapper.getDefectiveSampleByNo(sampleChildOrderNo, sampleOrderStatus, sampleResult);
    }

    public List<SampleChildOrderPo> getListByChildNoAndStatus(String sampleChildOrderNo,
                                                              SampleOrderStatus sampleOrderStatus) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNo)
                .eq(null != sampleOrderStatus, SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatus));
    }

    public List<SampleChildOrderPo> getSampleNoAndStatusBySpu(String spu) {
        if (StringUtils.isBlank(spu)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSpu, spu));
    }

    /**
     * 根据编号批量修改状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 16:20
     */
    public void updateBatchSampleChildOrderNo(List<String> sampleChildOrderNos, SampleOrderStatus sampleOrderStatus) {
        if (CollectionUtil.isNotEmpty(sampleChildOrderNos)) {
            final SampleChildOrderPo sampleChildOrderPo = new SampleChildOrderPo();
            sampleChildOrderPo.setSampleOrderStatus(sampleOrderStatus);
            baseMapper.update(sampleChildOrderPo, Wrappers.<SampleChildOrderPo>lambdaUpdate()
                    .in(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNos));
        }
    }

    /**
     * 通过供应商代码获取对应的列表
     *
     * @author ChenWenLong
     * @date 2023/1/30 14:31
     */
    public List<SampleChildOrderPo> getBySupplierCode(String supplierCode) {
        return list(Wrappers.<SampleChildOrderPo>lambdaQuery().eq(SampleChildOrderPo::getSupplierCode, supplierCode));
    }

    public List<SampleChildOrderPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSku, skuList));
    }

    /**
     * 通过spu批量查询
     *
     * @author ChenWenLong
     * @date 2023/2/1 11:00
     */
    public List<SampleChildOrderPo> getListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSpu, spuList));
    }

    /**
     * 通过编号批量查询
     *
     * @author ChenWenLong
     * @date 2023/2/1 11:00
     */
    public List<SampleChildOrderPo> getBatchSampleChildOrderNo(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNoList));
    }

    public List<SampleChildOrderPo> getListBySpuAndStatus(String spuCode, List<SampleOrderStatus> sampleOrderStatusList) {
        if (StringUtils.isBlank(spuCode)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .eq(SampleChildOrderPo::getSpu, spuCode)
                .in(SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusList));
    }

    /**
     * 通过批量供应商代码获取对应的列表
     *
     * @author ChenWenLong
     * @date 2023/4/14 16:12
     */
    public List<SampleChildOrderPo> getBatchSupplierCode(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SampleChildOrderPo>lambdaQuery().in(SampleChildOrderPo::getSupplierCode, supplierCodeList));
    }

    public List<SampleChildOrderPo> getListBySkuAndStatus(List<String> skuList, List<SampleOrderStatus> sampleOrderStatusList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSku, skuList)
                .in(SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusList)
                .orderByDesc(SampleChildOrderPo::getCreateTime));
    }

    /**
     * 通过SKU查询并过滤指定的样品子单号
     *
     * @author ChenWenLong
     * @date 2023/5/12 17:23
     */
    public List<SampleChildOrderPo> getListBySkuAndStatusAndNotNo(List<String> skuList,
                                                                  List<SampleOrderStatus> sampleOrderStatusList,
                                                                  List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSku, skuList)
                .in(SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusList)
                .notIn(CollectionUtils.isNotEmpty(sampleChildOrderNoList), SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNoList)
                .orderByDesc(SampleChildOrderPo::getCreateTime));
    }

    /**
     * 通过批量SKU和开发类型查询
     *
     * @author ChenWenLong
     * @date 2023/5/16 17:06
     */
    public List<SampleListBySkuAndDevTypeVo> getListBySkuAndDevType(List<String> skuList,
                                                                    List<SampleDevType> sampleDevTypeList,
                                                                    List<SampleOrderStatus> sampleOrderStatusList,
                                                                    List<SampleProduceLabel> sampleProduceLabelList) {
        return baseMapper.getListBySkuAndDevType(skuList, sampleDevTypeList, sampleOrderStatusList, sampleProduceLabelList);
    }

    /**
     * 批量批次码查询
     *
     * @author ChenWenLong
     * @date 2023/5/30 15:56
     */
    public List<SampleChildOrderPo> getListBySkuBatchCode(List<String> skuBatchCodeList) {
        if (CollectionUtils.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSkuBatchCode, skuBatchCodeList));
    }


    public List<SampleChildOrderPo> getListByLikeSkuBatchCode(String skuBatchCode) {
        if (StringUtils.isBlank(skuBatchCode)) {
            return null;
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .like(SampleChildOrderPo::getSkuBatchCode, skuBatchCode));
    }

    public List<SampleChildOrderPo> getListByBatchStatusList(List<SampleOrderStatus> sampleOrderStatusList,
                                                             List<SampleProduceLabel> sampleProduceLabelList,
                                                             List<SampleDevType> sampleDevTypeList) {
        if (CollectionUtils.isEmpty(sampleOrderStatusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusList)
                .in(CollectionUtils.isNotEmpty(sampleProduceLabelList), SampleChildOrderPo::getSampleProduceLabel, sampleProduceLabelList)
                .in(CollectionUtils.isNotEmpty(sampleDevTypeList), SampleChildOrderPo::getSampleDevType, sampleDevTypeList));
    }

    public List<SampleChildOrderPo> getListBySampleNoList(List<String> sampleChildOrderNoList,
                                                          String supplierCode,
                                                          List<SampleOrderStatus> sampleOrderStatusNotList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderPo>lambdaQuery()
                .in(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderNoList)
                .eq(StringUtils.isNotBlank(supplierCode), SampleChildOrderPo::getSupplierCode, supplierCode)
                .notIn(CollectionUtil.isNotEmpty(sampleOrderStatusNotList), SampleChildOrderPo::getSampleOrderStatus, sampleOrderStatusNotList));
    }
}
