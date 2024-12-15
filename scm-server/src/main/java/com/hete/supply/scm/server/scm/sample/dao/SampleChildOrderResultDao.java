package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleChildOrderResultSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildResultExportVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderResultPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderResultSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 样品子单结果列表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-12-20
 */
@Component
@Validated
public class SampleChildOrderResultDao extends BaseDao<SampleChildOrderResultMapper, SampleChildOrderResultPo> {

    public List<SampleChildOrderResultPo> selectListByChildNo(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                .in(SampleChildOrderResultPo::getSampleChildOrderNo, sampleChildOrderNoList)
                .orderByDesc(SampleChildOrderResultPo::getCreateTime));
    }

    public Integer getCntByChildOrderNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return 0;
        }
        return baseMapper.selectCount(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                        .eq(SampleChildOrderResultPo::getSampleChildOrderNo, sampleChildOrderNo))
                .intValue();


    }

    public SampleChildOrderResultPo getLatestByChildNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                .eq(SampleChildOrderResultPo::getSampleChildOrderNo, sampleChildOrderNo)
                .orderByDesc(SampleChildOrderResultPo::getCreateTime)
                .last("limit 1"));
    }

    public Map<String, List<SampleChildOrderResultPo>> getMapByChildOrderNoList(List<String> sampleChildOrderNoList, SampleResult sampleResult) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                        .in(SampleChildOrderResultPo::getSampleChildOrderNo, sampleChildOrderNoList)
                        .eq(null != sampleResult, SampleChildOrderResultPo::getSampleResult, sampleResult)
                        .orderByDesc(SampleChildOrderResultPo::getCreateTime))
                .stream()
                .collect(Collectors.groupingBy(SampleChildOrderResultPo::getSampleChildOrderNo));
    }

    public List<SampleChildOrderResultPo> getListByNoList(Set<String> sampleResultNoList) {
        if (CollectionUtils.isEmpty(sampleResultNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                .in(SampleChildOrderResultPo::getSampleResultNo, sampleResultNoList));
    }

    public List<SampleChildOrderResultPo> getDefectiveSampleNoList(String sampleResultNo, List<SampleResult> sampleResultList) {
        return baseMapper.selectList(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                .like(StringUtils.isNotBlank(sampleResultNo), SampleChildOrderResultPo::getSampleResultNo, sampleResultNo)
                .eq(SampleChildOrderResultPo::getRelateOrderNo, StringUtils.EMPTY)
                .in(SampleChildOrderResultPo::getSampleResult, sampleResultList));
    }

    public SampleChildOrderResultPo getOneByResultNo(String sampleResultNo) {
        if (StringUtils.isBlank(sampleResultNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SampleChildOrderResultPo>lambdaQuery()
                .eq(SampleChildOrderResultPo::getSampleResultNo, sampleResultNo));

    }


    /**
     * 分页查询列表
     *
     * @author ChenWenLong
     * @date 2023/4/14 15:31
     */
    public CommonPageResult.PageInfo<SampleChildOrderResultSearchVo> searchSampleChildOrderResult(Page<Void> page, SampleChildOrderResultSearchDto dto) {
        IPage<SampleChildOrderResultSearchVo> pageResult = baseMapper.searchSampleChildOrderResult(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 导出
     *
     * @author ChenWenLong
     * @date 2023/5/15 16:10
     */
    public Integer getExportTotals(SampleChildOrderResultSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 导出
     *
     * @author ChenWenLong
     * @date 2023/5/15 16:10
     */
    public CommonPageResult.PageInfo<SampleChildResultExportVo> getExportList(Page<Void> page, SampleChildOrderResultSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getExportList(page, dto));
    }
}
