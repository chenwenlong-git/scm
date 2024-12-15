package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleParentExportVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求母单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class SampleParentOrderDao extends BaseDao<SampleParentOrderMapper, SampleParentOrderPo> {

    public CommonPageResult.PageInfo<SampleSearchVo> searchSample(Page<Void> page,
                                                                  SampleSearchDto dto,
                                                                  List<String> skuParentOrderNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.searchSample(page, dto, skuParentOrderNoList));
    }

    public SampleParentOrderPo getByParentNo(@NotBlank String sampleParentOrderNo) {
        return baseMapper.selectOne(Wrappers.<SampleParentOrderPo>lambdaQuery()
                .eq(SampleParentOrderPo::getSampleParentOrderNo, sampleParentOrderNo));
    }


    public List<SampleParentOrderPo> getByParentNoList(List<String> sampleParentNoList) {
        if (CollectionUtils.isEmpty(sampleParentNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderPo>lambdaQuery()
                .in(SampleParentOrderPo::getSampleParentOrderNo, sampleParentNoList));
    }

    public List<String> getDefectiveSampleNoSet() {
        return baseMapper.getDefectiveSampleNoSet();
    }

    public Integer getSampleParentExportTotals(SampleSearchDto dto,
                                               List<String> skuParentOrderNoList) {
        return baseMapper.getSampleParentExportTotals(dto, skuParentOrderNoList);
    }

    public CommonPageResult.PageInfo<SampleParentExportVo> getExportList(Page<Void> page, SampleSearchDto dto,
                                                                         List<String> skuParentOrderNoList) {
        return PageInfoUtil.getPageInfo(baseMapper.getExportList(page, dto, skuParentOrderNoList));
    }

    public List<SampleParentOrderPo> getListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderPo>lambdaQuery()
                .in(SampleParentOrderPo::getSpu, spuList));
    }

    public void updatePurchaseCnt(Long id, Integer version, int purchaseSum) {
        baseMapper.updatePurchaseCnt(id, version, purchaseSum);
    }

    public List<SampleParentOrderPo> getSampleNoAndStatusBySpu(String spu) {
        if (StringUtils.isBlank(spu)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderPo>lambdaQuery()
                .eq(SampleParentOrderPo::getSpu, spu));
    }

    /**
     * 通过平台批量查询列表
     *
     * @author ChenWenLong
     * @date 2023/4/14 16:26
     */
    public List<SampleParentOrderPo> getBatchPlatform(List<String> platformList) {
        if (CollectionUtils.isEmpty(platformList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderPo>lambdaQuery()
                .in(SampleParentOrderPo::getPlatform, platformList));
    }
}
