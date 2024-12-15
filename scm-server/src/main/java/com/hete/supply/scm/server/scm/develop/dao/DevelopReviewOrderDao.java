package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopReviewSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopReviewGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopReviewSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发审版单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Component
@Validated
public class DevelopReviewOrderDao extends BaseDao<DevelopReviewOrderMapper, DevelopReviewOrderPo> {


    public CommonPageResult.PageInfo<DevelopReviewSearchVo> searchDevelopReview(Page<Void> page, DevelopReviewSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.searchDevelopReview(page, dto));

    }

    public List<DevelopReviewOrderPo> getListByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .in(DevelopReviewOrderPo::getDevelopChildOrderNo, developChildOrderNoList));
    }

    public DevelopReviewOrderPo getOneByNo(String developReviewOrderNo) {
        if (StringUtils.isBlank(developReviewOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .eq(DevelopReviewOrderPo::getDevelopReviewOrderNo, developReviewOrderNo));
    }

    public List<DevelopReviewGroupByStatusBo> getListByGroupByStatus(List<DevelopReviewOrderType> typeList,
                                                                     List<String> supplierCodeList) {
        return baseMapper.getListByGroupByStatus(typeList, supplierCodeList);
    }

    public List<DevelopReviewOrderPo> getListByStatusList(List<DevelopReviewOrderStatus> statusList) {
        if (CollectionUtils.isEmpty(statusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .in(DevelopReviewOrderPo::getDevelopReviewOrderStatus, statusList));
    }

    public Integer getReviewExportTotals(DevelopReviewSearchDto dto) {
        return baseMapper.getReviewExportTotals(dto);
    }

    public List<DevelopReviewOrderPo> getListByNoList(List<String> developReviewOrderNoList) {
        if (CollectionUtils.isEmpty(developReviewOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .in(DevelopReviewOrderPo::getDevelopReviewOrderNo, developReviewOrderNoList));
    }

    public DevelopReviewOrderPo getOneByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .in(DevelopReviewOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public List<DevelopReviewOrderPo> getListByReviewResult(ReviewResult reviewResult) {
        if (reviewResult == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .eq(DevelopReviewOrderPo::getReviewResult, reviewResult));
    }

    public List<DevelopReviewOrderPo> getListByDevelopPamphletOrderNoList(List<String> developPamphletOrderNoList) {
        if (CollectionUtils.isEmpty(developPamphletOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .in(DevelopReviewOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNoList));
    }

    public DevelopReviewOrderPo getOneByNoAndVersion(String developReviewOrderNo, Integer version) {
        if (StringUtils.isBlank(developReviewOrderNo) || null == version) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<DevelopReviewOrderPo>lambdaQuery()
                .eq(DevelopReviewOrderPo::getDevelopReviewOrderNo, developReviewOrderNo)
                .eq(DevelopReviewOrderPo::getVersion, version));
    }
}
