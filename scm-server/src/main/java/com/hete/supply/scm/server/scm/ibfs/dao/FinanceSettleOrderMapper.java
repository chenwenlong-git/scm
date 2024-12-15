package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 财务结算单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Mapper
interface FinanceSettleOrderMapper extends BaseDataMapper<FinanceSettleOrderPo> {

    Page<FinanceSettleOrderPo> getByPage(Page<FinanceSettleOrderPo> page,
                                         @Param("paramDto") SearchSettleOrderDto searchDto);

    List<String> findSupCodesWithAuthSupCode(@Param("param") SearchSettleOrderDto searchDto);

    List<String> findSupCodesOptAuthSupCode(@Param("param") SearchSettleOrderDto searchDto);

    List<String> findSoCodesWithAuthSupCode(@Param("param") SearchSettleOrderDto searchDto);

    List<String> findSoCodesOptAuthSupCode(@Param("param") SearchSettleOrderDto searchDto);

    Page<FinanceSettleOrderPo> findPageOptAuthSupCode(Page<FinanceSettleOrderPo> page,
                                                      @Param("param") SearchSettleOrderDto searchDto);

    Page<FinanceSettleOrderPo> findPageWithAuthSupCode(Page<FinanceSettleOrderPo> page,
                                                       @Param("param") SearchSettleOrderDto searchDto);

    Integer findExportSettleOrderTotalCount(@Param("param") SearchSettleOrderDto dto);

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

    BigDecimal getAvgMonthSettleAmount(@Param("supplierCode") String supplierCode);
}
