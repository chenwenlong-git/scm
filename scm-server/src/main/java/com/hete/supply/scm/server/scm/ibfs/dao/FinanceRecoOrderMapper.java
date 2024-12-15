package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportItemVo;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportVo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderBatchUpdateInfoBo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 财务对账单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Mapper
interface FinanceRecoOrderMapper extends BaseDataMapper<FinanceRecoOrderPo> {

    IPage<RecoOrderSearchVo> searchRecoOrderPage(Page<Void> page, @Param("dto") RecoOrderSearchDto dto);

    Integer getExportTotals(@Param("dto") RecoOrderSearchDto dto);

    IPage<RecoOrderExportVo> getExportList(Page<Void> page, @Param("dto") RecoOrderSearchDto dto);

    Integer getExportTotalsItem(@Param("dto") RecoOrderSearchDto dto);

    IPage<RecoOrderExportItemVo> getExportListItem(Page<Void> page, @Param("dto") RecoOrderSearchDto dto);

    List<String> getSupplierList(@Param("dto") RecoOrderSearchDto dto);

    void updateInfoByBatchId(@Param("idList") List<Long> idList, @Param("bo") RecoOrderBatchUpdateInfoBo bo);
}
