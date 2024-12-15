package com.hete.supply.scm.server.supplier.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.SampleReturnExportVo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品退货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SampleReturnOrderDao extends BaseDao<SampleReturnOrderMapper, SampleReturnOrderPo> {

    public CommonPageResult.PageInfo<SampleReturnVo> searchProductPurchase(Page<Void> page, SampleReturnDto dto, List<String> sampleReturnOrderNoList) {

        return PageInfoUtil.getPageInfo(baseMapper.searchProductPurchase(page, dto, sampleReturnOrderNoList));
    }


    public SampleReturnOrderPo getOneByNo(String sampleReturnOrderNo) {
        if (StringUtils.isBlank(sampleReturnOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SampleReturnOrderPo>lambdaQuery()
                .eq(SampleReturnOrderPo::getSampleReturnOrderNo, sampleReturnOrderNo));
    }

    public List<SampleReturnOrderPo> getListByNoList(List<String> sampleReturnOrderNoList) {
        if (CollectionUtils.isEmpty(sampleReturnOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReturnOrderPo>lambdaQuery()
                .in(SampleReturnOrderPo::getSampleReturnOrderNo, sampleReturnOrderNoList)
                .orderByDesc(SampleReturnOrderPo::getCreateTime));
    }

    public List<SampleReturnOrderPo> getBySampleReturnOrderNo(String sampleReturnOrderNo, String supplierCode, List<ReceiptOrderStatus> receiptOrderStatusNotList) {
        if (StringUtils.isBlank(sampleReturnOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleReturnOrderPo>lambdaQuery()
                .like(SampleReturnOrderPo::getSampleReturnOrderNo, sampleReturnOrderNo)
                .eq(SampleReturnOrderPo::getSupplierCode, supplierCode)
                .notIn(SampleReturnOrderPo::getReturnOrderStatus, receiptOrderStatusNotList));
    }

    public Integer getExportTotals(SampleReturnDto dto, List<String> sampleReturnOrderNoList) {
        return baseMapper.getExportTotals(dto, sampleReturnOrderNoList);
    }

    public CommonPageResult.PageInfo<SampleReturnExportVo> getExportList(Page<Void> page, SampleReturnDto dto, List<String> sampleReturnOrderNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.getExportList(page, dto, sampleReturnOrderNoList));
    }

    public List<SampleReturnOrderPo> getListByTrackingNo(String trackingNo) {
        if (StringUtils.isBlank(trackingNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReturnOrderPo>lambdaQuery()
                .eq(SampleReturnOrderPo::getTrackingNo, trackingNo));

    }

    public List<SampleReturnOrderPo> getListByNoListAndSupplierCode(String supplierCode, List<String> sampleReturnOrderNoList) {
        if (CollectionUtils.isEmpty(sampleReturnOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReturnOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(supplierCode), SampleReturnOrderPo::getSupplierCode, supplierCode)
                .in(SampleReturnOrderPo::getSampleReturnOrderNo, sampleReturnOrderNoList)
                .orderByDesc(SampleReturnOrderPo::getCreateTime));
    }
}
