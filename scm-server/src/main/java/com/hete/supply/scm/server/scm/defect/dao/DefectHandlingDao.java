package com.hete.supply.scm.server.scm.defect.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DefectHandingSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 次品处理 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-06-21
 */
@Component
@Validated
public class DefectHandlingDao extends BaseDao<DefectHandlingMapper, DefectHandlingPo> {

    public IPage<DefectHandlingPo> searchDefect(Page<DefectHandlingPo> page, DefectHandingSearchDto dto) {
        return baseMapper.selectPage(page, Wrappers.<DefectHandlingPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(dto.getDefectHandlingNo()),
                        DefectHandlingPo::getDefectHandlingNo, dto.getDefectHandlingNo())
                .in(CollectionUtils.isNotEmpty(dto.getDefectHandlingNoList()),
                        DefectHandlingPo::getDefectHandlingNo, dto.getDefectHandlingNoList())
                .eq(StringUtils.isNotBlank(dto.getSku()),
                        DefectHandlingPo::getSku, dto.getSku())
                .eq(StringUtils.isNotBlank(dto.getSkuBatchCode()),
                        DefectHandlingPo::getSkuBatchCode, dto.getSkuBatchCode())
                .eq(StringUtils.isNotBlank(dto.getDefectBizNo()),
                        DefectHandlingPo::getDefectBizNo, dto.getDefectBizNo())
                .eq(StringUtils.isNotBlank(dto.getRelatedOrderNo()),
                        DefectHandlingPo::getRelatedOrderNo, dto.getRelatedOrderNo())
                .in(CollectionUtils.isNotEmpty(dto.getDefectHandlingProgrammeList()),
                        DefectHandlingPo::getDefectHandlingProgramme, dto.getDefectHandlingProgrammeList())
                .ge(null != dto.getConfirmTimeStart(),
                        DefectHandlingPo::getConfirmTime, dto.getConfirmTimeStart())
                .le(null != dto.getConfirmTimeEnd(),
                        DefectHandlingPo::getConfirmTime, dto.getConfirmTimeEnd())
                .ge(null != dto.getCreateTimeStart(),
                        DefectHandlingPo::getCreateTime, dto.getCreateTimeStart())
                .le(null != dto.getCreateTimeEnd(),
                        DefectHandlingPo::getCreateTime, dto.getCreateTimeEnd())
                .in(CollectionUtils.isNotEmpty(dto.getDefectHandlingStatusList()),
                        DefectHandlingPo::getDefectHandlingStatus, dto.getDefectHandlingStatusList())
                .eq(StringUtils.isNotBlank(dto.getQcOrderNo()),
                        DefectHandlingPo::getQcOrderNo, dto.getQcOrderNo())
                .in(CollectionUtils.isNotEmpty(dto.getSkuList()),
                        DefectHandlingPo::getSku, dto.getSkuList())
                .in(CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList()),
                        DefectHandlingPo::getSkuBatchCode, dto.getSkuBatchCodeList())
                .in(CollectionUtils.isNotEmpty(dto.getDefectHandlingTypeList()),
                        DefectHandlingPo::getDefectHandlingType, dto.getDefectHandlingTypeList())
                .orderByDesc(DefectHandlingPo::getCreateTime)
        );

    }

    public List<DefectHandlingPo> selectByDefectHandlingNoList(List<String> defectHandlingNoList) {
        if (CollectionUtils.isEmpty(defectHandlingNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .in(DefectHandlingPo::getDefectHandlingNo, defectHandlingNoList));
    }

    /**
     * 通过单号查询次品记录
     *
     * @param defectHandlingNos
     * @return
     */
    public List<DefectHandlingPo> getByDefectHandlingNos(List<String> defectHandlingNos) {
        if (CollectionUtils.isEmpty(defectHandlingNos)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<DefectHandlingPo>lambdaQuery()
                .in(DefectHandlingPo::getDefectHandlingNo, defectHandlingNos));
    }

    public DefectHandlingPo getByDefectHandlingNo(String defectHandlingNo) {
        return baseMapper.selectOne(Wrappers.<DefectHandlingPo>lambdaQuery()
                .eq(DefectHandlingPo::getDefectHandlingNo, defectHandlingNo));
    }

    public List<DefectHandlingPo> selectListByQcOrderNo(String qcOrderNo) {

        if (StringUtils.isBlank(qcOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .eq(DefectHandlingPo::getQcOrderNo, qcOrderNo));
    }

    public List<DefectHandlingPo> listByQcOrderNoList(Collection<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .in(DefectHandlingPo::getQcOrderNo, qcOrderNoList));
    }

    public List<DefectHandlingPo> listByQcOrderNo(String qcOrderNo) {
        if (StringUtils.isBlank(qcOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .eq(DefectHandlingPo::getQcOrderNo, qcOrderNo));
    }

    public List<DefectHandlingPo> getListByRelatedOrderNo(String relatedOrderNo) {

        if (StringUtils.isBlank(relatedOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .eq(DefectHandlingPo::getRelatedOrderNo, relatedOrderNo));
    }

    public List<DefectHandlingPo> listByQcDetailIds(List<Long> qcDetailIds,
                                                    DefectHandlingProgramme defectHandlingProgramme) {
        if (CollectionUtils.isEmpty(qcDetailIds)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .eq(DefectHandlingPo::getDefectHandlingProgramme, defectHandlingProgramme)
                .in(DefectHandlingPo::getBizDetailId, qcDetailIds));
    }


    public List<DefectHandlingPo> getListByInitSkuBatchPrice() {
        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .ne(DefectHandlingPo::getReceiveOrderNo, "")
                .ne(DefectHandlingPo::getSkuBatchCode, "")
                .eq(DefectHandlingPo::getDefectHandlingProgramme, DefectHandlingProgramme.EXCHANGE_GOODS));
    }

    public List<DefectHandlingPo> getListByRelatedOrderNoList(List<String> relatedOrderNoList) {
        if (CollectionUtils.isEmpty(relatedOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DefectHandlingPo>lambdaQuery()
                .in(DefectHandlingPo::getRelatedOrderNo, relatedOrderNoList));

    }
}
