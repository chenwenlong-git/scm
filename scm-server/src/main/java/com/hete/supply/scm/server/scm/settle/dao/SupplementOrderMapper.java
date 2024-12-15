package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.bo.SupplementOrderPurchaseExportBo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 补款单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface SupplementOrderMapper extends BaseDataMapper<SupplementOrderPo> {

    /**
     * 分页查询补款单信息
     *
     * @author ChenWenLong
     * @date 2022/11/4 10:23
     */
    IPage<SupplementOrderVo> selectSupplementOrderPage(Page<Void> page, @Param("dto") SupplementOrderDto supplementOrderDto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("dto") SupplementOrderQueryByApiDto dto);

    /**
     * 统计导出的列表
     *
     * @param dto
     * @return
     */
    IPage<SupplementOrderExportVo> getExportList(Page<Void> page, @Param("dto") SupplementOrderQueryByApiDto dto);

    List<SupplementOrderPurchaseExportBo> getPurchaseBatchSupplementOrderNo(@Param("supplementOrderNoList") List<String> supplementOrderNoList);

    /**
     * 按sku导出
     *
     * @param dto
     * @return
     */
    Integer getExportSkuTotals(@Param("dto") SupplementOrderDto dto);

    /**
     * 按sku导出列表
     *
     * @param dto
     * @return
     */
    IPage<SupplementOrderPo> getExportSkuList(Page<Void> page, @Param("dto") SupplementOrderDto dto);

}
