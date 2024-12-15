package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.PrepaymentExportVo;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePrepaymentOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.PrepaymentSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 预付款单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-10
 */
@Mapper
interface FinancePrepaymentOrderMapper extends BaseDataMapper<FinancePrepaymentOrderPo> {

    IPage<PrepaymentSearchVo> searchPrepayment(Page<Void> page, @Param("dto") PrepaymentSearchDto dto);

    BigDecimal getAllCanDeductionMoney(@Param("supplierCode") String supplierCode,
                                       @Param("prepaymentOrderStatus") PrepaymentOrderStatus prepaymentOrderStatus);

    List<String> getSupplierList(@Param("dto") PrepaymentSearchDto dto);

    BigDecimal getRecentlyPrepaymentMoney(@Param("supplierCode") String supplierCode,
                                          @Param("prepaymentOrderStatus") PrepaymentOrderStatus prepaymentOrderStatus,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    Long getApproveFailTimesByFollowUser(@Param("feishuAuditOrderType") FeishuAuditOrderType feishuAuditOrderType,
                                         @Param("followUser") String followUser,
                                         @Param("workflowResult") WorkflowResult workflowResult,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    Long getApproveFailTimesBySupplier(@Param("feishuAuditOrderType") FeishuAuditOrderType feishuAuditOrderType,
                                       @Param("supplierCode") String supplierCode,
                                       @Param("workflowResult") WorkflowResult workflowResult,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    Integer getExportTotals(@Param("dto") PrepaymentSearchDto dto);

    IPage<PrepaymentExportVo> getExportList(Page<Void> page, @Param("dto") PrepaymentSearchDto dto);
}
