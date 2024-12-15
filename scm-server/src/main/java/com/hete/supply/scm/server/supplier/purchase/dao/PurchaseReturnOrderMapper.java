package com.hete.supply.scm.server.supplier.purchase.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseReturnExportVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSearchVo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 采购退货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface PurchaseReturnOrderMapper extends BaseDataMapper<PurchaseReturnOrderPo> {

    IPage<PurchaseReturnExportVo> getExportList(Page<Void> page, @Param("dto") PurchaseReturnDto dto);

    Integer getExportTotals(@Param("dto") PurchaseReturnDto dto);

    IPage<PurchaseReturnSearchVo> searchPurchaseReturnPage(Page<Void> page, @Param("dto") PurchaseReturnDto dto);

    BigDecimal getReturnMoney(@Param("supplierCode") String supplierCode,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime,
                              @Param("returnTypeList") List<ReturnType> returnTypeList);
}
