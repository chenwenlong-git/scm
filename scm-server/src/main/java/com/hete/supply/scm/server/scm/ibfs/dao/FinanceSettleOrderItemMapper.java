package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderItemPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 财务结算单明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Mapper
interface FinanceSettleOrderItemMapper extends BaseDataMapper<FinanceSettleOrderItemPo> {

    IPage<FinanceSettleOrderItemPo> findPageSettleOrderItems(Page<FinanceSettleOrderItemPo> page,
                                                             @Param("param") SearchSettleOrderDto dto);

    Integer findExportSettleItemTotalCount(@Param("param") SearchSettleOrderDto dto);
}
