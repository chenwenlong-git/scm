package com.hete.supply.scm.server.scm.qc.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcDetailExportBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcWaitDetailBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 质检单详情 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Component
@Validated
public class QcDetailDao extends BaseDao<QcDetailMapper, QcDetailPo> {

    public List<QcDetailPo> getListByQcOrderNo(String qcOrderNo) {
        if (StrUtil.isBlank(qcOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<QcDetailPo>lambdaQuery().eq(QcDetailPo::getQcOrderNo, qcOrderNo));
    }

    public List<QcDetailPo> getQcDetailListBySearchDto(QcSearchDto dto) {
        // 不存在查询条件的情况直接返回null
        if (CollectionUtils.isEmpty(dto.getContainerCodeList()) && CollectionUtils.isEmpty(dto.getSkuBatchCodeList())
                && CollectionUtils.isEmpty(dto.getSkuList()) && StringUtils.isBlank(dto.getSku()) && StringUtils.isBlank(dto.getCategoryName())) {
            return null;
        }

        return baseMapper.selectList(Wrappers.<QcDetailPo>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(dto.getContainerCodeList()),
                        QcDetailPo::getContainerCode, dto.getContainerCodeList())
                .in(CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList()),
                        QcDetailPo::getBatchCode, dto.getSkuBatchCodeList())
                .in(CollectionUtils.isNotEmpty(dto.getSkuList()),
                        QcDetailPo::getSkuCode, dto.getSkuList())
                .like(StringUtils.isNotBlank(dto.getSku()), QcDetailPo::getSkuCode,
                        dto.getSku())
                .like(StringUtils.isNotBlank(dto.getCategoryName()), QcDetailPo::getCategoryName, dto.getCategoryName()));
    }

    public List<QcDetailPo> getListByQcOrderNoList(Collection<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcDetailPo>lambdaQuery()
                .in(QcDetailPo::getQcOrderNo, qcOrderNoList));

    }

    public void removeUnPassedByQcOrderNo(String qcOrderNo) {
        if (StringUtils.isBlank(qcOrderNo)) {
            return;
        }

        baseMapper.deleteSkipCheck(Wrappers.<QcDetailPo>lambdaUpdate()
                .eq(QcDetailPo::getQcOrderNo, qcOrderNo)
                .eq(QcDetailPo::getQcResult, QcResult.NOT_PASSED));
    }

    public List<QcDetailPo> getUnPassedListByQcOrderNoList(List<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcDetailPo>lambdaQuery()
                .in(QcDetailPo::getQcOrderNo, qcOrderNoList)
                .eq(QcDetailPo::getQcResult, QcResult.NOT_PASSED));
    }

    public void removeUnPassedByQcOrderNoList(List<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return;
        }

        baseMapper.deleteSkipCheck(Wrappers.<QcDetailPo>lambdaUpdate()
                .in(QcDetailPo::getQcOrderNo, qcOrderNoList)
                .eq(QcDetailPo::getQcResult, QcResult.NOT_PASSED));
    }

    public List<QcDetailPo> getListByContainerCode(String containerCode) {
        if (StringUtils.isBlank(containerCode)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcDetailPo>lambdaUpdate()
                .eq(QcDetailPo::getContainerCode, containerCode)
                .orderByDesc(QcDetailPo::getCreateTime));
    }

    /**
     * 根据质检单号列表查询质检明细并返回分页数据。
     *
     * @param page        分页信息
     * @param qcSearchDto 导出查询条件
     * @return 质检明细的分页数据
     */
    public IPage<QcDetailExportBo> getExportPage(Page<QcDetailPo> page, QcSearchDto qcSearchDto) {
        // 调用底层数据访问层的方法，获取质检明细导出列表的分页数据
        return baseMapper.getQcDetailExportList(page, qcSearchDto);
    }

    public List<QcDetailPo> getListByIdList(List<Long> qcDetailIdList) {
        if (CollectionUtils.isEmpty(qcDetailIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectBatchIds(qcDetailIdList);
    }

    public int getExportCount(QcSearchDto qcSearchDto) {
        return baseMapper.getQcDetailExportTotals(qcSearchDto);
    }

    public List<QcWaitDetailBo> getQcWaitDetail(List<String> skuCodeList, List<QcState> qcStateList) {
        return baseMapper.getQcWaitDetail(skuCodeList, qcStateList);
    }
}
