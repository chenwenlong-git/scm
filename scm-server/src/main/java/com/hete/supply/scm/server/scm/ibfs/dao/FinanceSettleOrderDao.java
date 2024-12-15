package com.hete.supply.scm.server.scm.ibfs.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务结算单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceSettleOrderDao extends BaseDao<FinanceSettleOrderMapper, FinanceSettleOrderPo> {

    public FinanceSettleOrderPo findByOrderNoAndVersion(String financeSettleOrderNo,
                                                        Integer version) {
        return baseMapper.selectOne(Wrappers.<FinanceSettleOrderPo>lambdaQuery()
                .eq(FinanceSettleOrderPo::getFinanceSettleOrderNo, financeSettleOrderNo)
                .eq(FinanceSettleOrderPo::getVersion, version));
    }

    public List<FinanceSettleOrderPo> findByOrderNos(Collection<String> financeSettleOrderNos) {
        if (CollectionUtils.isEmpty(financeSettleOrderNos)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceSettleOrderPo>lambdaQuery()
                .in(FinanceSettleOrderPo::getFinanceSettleOrderNo, financeSettleOrderNos));
    }

    public FinanceSettleOrderPo findByOrderNo(String settleOrderNo) {
        if (StrUtil.isBlank(settleOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<FinanceSettleOrderPo>lambdaQuery()
                .eq(FinanceSettleOrderPo::getFinanceSettleOrderNo,
                        settleOrderNo));
    }

    public List<String> findSupCodesWithAuthSupCode(SearchSettleOrderDto searchDto) {
        return baseMapper.findSupCodesWithAuthSupCode(searchDto);
    }

    public List<String> findSupCodesOptAuthSupCode(SearchSettleOrderDto searchDto) {
        return baseMapper.findSupCodesOptAuthSupCode(searchDto);
    }

    public List<String> findSoCodesWithAuthSupCode(SearchSettleOrderDto searchDto) {
        return baseMapper.findSoCodesWithAuthSupCode(searchDto);
    }

    public List<String> findSoCodesOptAuthSupCode(SearchSettleOrderDto searchDto) {
        return baseMapper.findSoCodesOptAuthSupCode(searchDto);
    }

    public Page<FinanceSettleOrderPo> findPageOptAuthSupCode(SearchSettleOrderDto searchDto) {
        return baseMapper.findPageOptAuthSupCode(PageDTO.of(searchDto.getPageNo(), searchDto.getPageSize()), searchDto);
    }

    public Page<FinanceSettleOrderPo> findPageWithAuthSupCode(SearchSettleOrderDto searchDto) {
        return baseMapper.findPageWithAuthSupCode(PageDTO.of(searchDto.getPageNo(), searchDto.getPageSize()),
                searchDto);
    }

    public Integer getExportSettleOrderTotalCount(SearchSettleOrderDto dto) {
        return baseMapper.findExportSettleOrderTotalCount(dto);
    }

    public Long getApproveFailTimesByFollowUser(FeishuAuditOrderType feishuAuditOrderType,
                                                String followUser,
                                                LocalDateTime startTime,
                                                LocalDateTime endTime) {
        return baseMapper.getApproveFailTimesByFollowUser(feishuAuditOrderType, followUser, WorkflowResult.REFUSE,
                startTime, endTime);
    }

    public Long getApproveFailTimesBySupplier(FeishuAuditOrderType feishuAuditOrderType,
                                              String supplierCode,
                                              LocalDateTime startTime,
                                              LocalDateTime endTime) {
        return baseMapper.getApproveFailTimesBySupplier(feishuAuditOrderType, supplierCode, WorkflowResult.REFUSE,
                startTime, endTime);
    }

    public BigDecimal getAvgMonthSettleAmount(String supplierCode) {
        return baseMapper.getAvgMonthSettleAmount(supplierCode);
    }
}
