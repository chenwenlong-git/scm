package com.hete.supply.scm.server.supplier.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleDeliverExportVo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverVo;
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
 * 样品发货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SampleDeliverOrderDao extends BaseDao<SampleDeliverOrderMapper, SampleDeliverOrderPo> {

    public CommonPageResult.PageInfo<SampleDeliverVo> searchDeliver(Page<Void> page, SampleDeliverSearchDto dto,
                                                                    List<String> deliverNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.searchDeliver(page, dto, deliverNoList));
    }

    public SampleDeliverOrderPo getDeliverPoByNo(String sampleDeliverOrderNo) {

        return baseMapper.selectOne(Wrappers.<SampleDeliverOrderPo>lambdaQuery()
                .eq(SampleDeliverOrderPo::getSampleDeliverOrderNo, sampleDeliverOrderNo));
    }

    public List<SampleDeliverOrderPo> getDeliverPoListByNoList(List<String> sampleDeliverOrderNoList) {
        if (CollectionUtils.isEmpty(sampleDeliverOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleDeliverOrderPo>lambdaQuery()
                .in(SampleDeliverOrderPo::getSampleDeliverOrderNo, sampleDeliverOrderNoList)
                .orderByDesc(SampleDeliverOrderPo::getCreateTime));
    }

    public Integer getExportTotals(SampleDeliverSearchDto dto, List<String> deliverNoList) {
        return baseMapper.getExportTotals(dto, deliverNoList);
    }

    public CommonPageResult.PageInfo<SampleDeliverExportVo> getExportList(Page<Void> page, SampleDeliverSearchDto dto, List<String> deliverNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.getExportList(page, dto, deliverNoList));
    }

    public List<SampleDeliverOrderPo> getListByTrackingNo(String trackingNo) {
        if (StringUtils.isBlank(trackingNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleDeliverOrderPo>lambdaQuery()
                .eq(SampleDeliverOrderPo::getTrackingNo, trackingNo));
    }
}
