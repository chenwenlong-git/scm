package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReceiptExportVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品收货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Component
@Validated
public class SampleReceiptOrderDao extends BaseDao<SampleReceiptOrderMapper, SampleReceiptOrderPo> {

    public CommonPageResult.PageInfo<SampleReceiptSearchVo> searchSampleReceipt(Page<Void> page, SampleReceiptSearchDto dto,
                                                                                List<String> sampleReceiptOrderNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.searchSampleReceipt(page, dto, sampleReceiptOrderNoList));
    }

    public SampleReceiptOrderPo getOneByDeliverOrderNo(@NotBlank String sampleDeliverOrderNo) {
        return baseMapper.selectOne(Wrappers.<SampleReceiptOrderPo>lambdaQuery()
                .eq(SampleReceiptOrderPo::getSampleDeliverOrderNo, sampleDeliverOrderNo));
    }

    public SampleReceiptOrderPo getOneByReceiptOrderNo(@NotBlank String sampleReceiptOrderNo) {
        return baseMapper.selectOne(Wrappers.<SampleReceiptOrderPo>lambdaQuery()
                .eq(SampleReceiptOrderPo::getSampleReceiptOrderNo, sampleReceiptOrderNo));
    }

    public List<SampleReceiptOrderPo> getListByReceiptNoList(List<String> receiptOrderNoList) {
        if (CollectionUtils.isEmpty(receiptOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleReceiptOrderPo>lambdaQuery()
                .in(SampleReceiptOrderPo::getSampleReceiptOrderNo, receiptOrderNoList));
    }

    public Integer getExportTotals(SampleReceiptSearchDto dto, List<String> sampleReceiptOrderNoList) {
        return baseMapper.getExportTotals(dto, sampleReceiptOrderNoList);
    }

    public CommonPageResult.PageInfo<SampleReceiptExportVo> getExportList(Page<Void> page, SampleReceiptSearchDto dto, List<String> sampleReceiptOrderNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.getExportList(page, dto, sampleReceiptOrderNoList));
    }

    public List<SampleReceiptOrderPo> getListByTrackingNo(String trackingNo) {
        if (StringUtils.isBlank(trackingNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReceiptOrderPo>lambdaQuery()
                .eq(SampleReceiptOrderPo::getTrackingNo, trackingNo));
    }
}
