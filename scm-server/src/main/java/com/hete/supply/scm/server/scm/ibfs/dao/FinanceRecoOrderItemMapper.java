package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderItemSearchDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemSearchVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemTotalVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 财务对账单明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Mapper
interface FinanceRecoOrderItemMapper extends BaseDataMapper<FinanceRecoOrderItemPo> {

    IPage<RecoOrderItemSearchVo> searchRecoOrderItemPage(Page<Void> page, @Param("dto") RecoOrderItemSearchDto dto);

    Integer getRecoOrderItemTotalGroup(@Param("dto") RecoOrderItemSearchDto dto);

    Integer getRecoOrderItemTotalGroupSku(@Param("dto") RecoOrderItemSearchDto dto);

    List<String> getRecoOrderItemTotalGroupSkuList(@Param("dto") RecoOrderItemSearchDto dto);

    RecoOrderItemTotalVo getRecoOrderItemTotal(@Param("dto") RecoOrderItemSearchDto dto);
}
