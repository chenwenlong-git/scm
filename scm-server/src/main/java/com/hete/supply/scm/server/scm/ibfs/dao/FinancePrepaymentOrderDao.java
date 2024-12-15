package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.PrepaymentExportVo;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePrepaymentOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.PrepaymentSearchVo;
import com.hete.supply.scm.server.scm.ibfs.enums.DeductionStatus;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 预付款单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-10
 */
@Component
@Validated
public class FinancePrepaymentOrderDao extends BaseDao<FinancePrepaymentOrderMapper, FinancePrepaymentOrderPo> {

    public CommonPageResult.PageInfo<PrepaymentSearchVo> searchPrepayment(Page<Void> page, PrepaymentSearchDto dto) {
        final IPage<PrepaymentSearchVo> pageResult = baseMapper.searchPrepayment(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public FinancePrepaymentOrderPo getOneByNo(String prepaymentOrderNo) {
        if (StringUtils.isBlank(prepaymentOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<FinancePrepaymentOrderPo>lambdaQuery()
                .eq(FinancePrepaymentOrderPo::getPrepaymentOrderNo, prepaymentOrderNo));

    }

    public List<FinancePrepaymentOrderPo> getListByNoList(List<String> prepaymentOrderNoList) {
        if (CollectionUtils.isEmpty(prepaymentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<FinancePrepaymentOrderPo>lambdaQuery()
                .in(FinancePrepaymentOrderPo::getPrepaymentOrderNo, prepaymentOrderNoList));
    }

    public BigDecimal getAllCanDeductionMoney(String supplierCode, PrepaymentOrderStatus prepaymentOrderStatus) {
        if (StringUtils.isBlank(supplierCode)) {
            return BigDecimal.ZERO;
        }

        return baseMapper.getAllCanDeductionMoney(supplierCode, prepaymentOrderStatus);
    }

    public List<String> getSupplierList(PrepaymentSearchDto dto) {
        return baseMapper.getSupplierList(dto);
    }

    public BigDecimal getRecentlyPrepaymentMoney(String supplierCode, PrepaymentOrderStatus prepaymentOrderStatus,
                                                 LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.getRecentlyPrepaymentMoney(supplierCode, prepaymentOrderStatus, startTime, endTime);
    }

    public Long getApproveFailTimesByFollowUser(FeishuAuditOrderType feishuAuditOrderType, String followUser,
                                                LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.getApproveFailTimesByFollowUser(feishuAuditOrderType, followUser, WorkflowResult.REFUSE,
                startTime, endTime);
    }

    public Long getApproveFailTimesBySupplier(FeishuAuditOrderType feishuAuditOrderType, String supplierCode,
                                              LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.getApproveFailTimesBySupplier(feishuAuditOrderType, supplierCode, WorkflowResult.REFUSE,
                startTime, endTime);
    }

    public Integer getExportTotals(PrepaymentSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PrepaymentExportVo> getExportList(Page<Void> page, PrepaymentSearchDto dto) {
        IPage<PrepaymentExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<FinancePrepaymentOrderPo> getPrepaymentBoBySupplier(String supplierCode, LocalDateTime createTimeStart,
                                                                    LocalDateTime createTimeEnd,
                                                                    PrepaymentOrderStatus prepaymentOrderStatus,
                                                                    DeductionStatus deductionStatus) {
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<FinancePrepaymentOrderPo>lambdaQuery()
                .eq(FinancePrepaymentOrderPo::getSupplierCode, supplierCode)
                .eq(FinancePrepaymentOrderPo::getPrepaymentOrderStatus, prepaymentOrderStatus)
                .eq(FinancePrepaymentOrderPo::getDeductionStatus, deductionStatus)
                .ge(null != createTimeStart, FinancePrepaymentOrderPo::getCreateTime, createTimeStart)
                .le(null != createTimeEnd, FinancePrepaymentOrderPo::getCreateTime, createTimeEnd));
    }
}
