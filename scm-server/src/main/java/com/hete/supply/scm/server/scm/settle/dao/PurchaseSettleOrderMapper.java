package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 采购结算单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Mapper
interface PurchaseSettleOrderMapper extends BaseDataMapper<PurchaseSettleOrderPo> {

    /**
     * 分页查询信息
     *
     * @author ChenWenLong
     * @date 2022/11/4 10:23
     */
    IPage<PurchaseSettleOrderVo> selectPurchaseSettleOrderPage(Page<Void> page, @Param("dto") PurchaseSettleOrderSearchDto purchaseSettleOrderSearchDto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("dto") PurchaseSettleOrderSearchDto dto);

    /**
     * 导出的列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<PurchaseSettleOrderExportVo> getExportList(Page<Void> page, @Param("dto") PurchaseSettleOrderSearchDto dto);

}
