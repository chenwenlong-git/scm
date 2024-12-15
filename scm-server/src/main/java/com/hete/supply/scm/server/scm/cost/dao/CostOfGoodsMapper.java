package com.hete.supply.scm.server.scm.cost.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuItemDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.api.scm.entity.vo.GoodsOfCostExportVo;
import com.hete.supply.scm.server.scm.cost.entity.bo.GoodsCostBo;
import com.hete.supply.scm.server.scm.cost.entity.bo.SkuAndWarehouseBatchBo;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostLogItemVo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品成本表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-20
 */
@Mapper
interface CostOfGoodsMapper extends BaseDataMapper<CostOfGoodsPo> {

    IPage<GoodsCostVo> searchGoodsOfCost(Page<Void> page, @Param("dto") GoodsCostDto dto);

    List<GoodsCostBo> getRelatedCostData(@Param("goodsCostVoList") List<GoodsCostVo> goodsCostVoList,
                                         @Param("timeStr") String timeStr,
                                         @Param("costTimeType") CostTimeType costTimeType,
                                         @Param("polymerizeType") PolymerizeType polymerizeType);

    IPage<GoodsCostLogItemVo> searchGoodsOfCostLogs(Page<Void> page,
                                                    @Param("sku") String sku,
                                                    @Param("polymerizeType") PolymerizeType polymerizeType,
                                                    @Param("costTimeType") CostTimeType costTimeType,
                                                    @Param("polymerizeWarehouse") PolymerizeWarehouse polymerizeWarehouse,
                                                    @Param("warehouseCode") String warehouseCode);

    IPage<GoodsOfCostExportVo> searchGoodsOfCostExport(Page<Void> page, @Param("dto") GoodsCostDto dto,
                                                       @Param("timeStr") String timeStr,
                                                       @Param("costTimeType") CostTimeType costTimeType);

    List<CostOfGoodsPo> getSkuCostBySkuAndWarehouse(@Param("timeStr") String timeStr,
                                                    @Param("costTimeType") CostTimeType costTimeType,
                                                    @Param("polymerizeType") PolymerizeType polymerizeType,
                                                    @Param("dtoList") List<CostSkuItemDto> singWarehouseDtoList);

    List<CostOfGoodsPo> getMonthCostNotExist(@Param("lastMonthStr") String lastMonthStr,
                                             @Param("currentMonthString") String currentMonthString);

    List<CostOfGoodsPo> getMonthCostBySkuAndWarehouse(@Param("costOfGoodsPoList") List<CostOfGoodsPo> costOfGoodsPoList,
                                                      @Param("costTime") String costTime);

    List<CostOfGoodsPo> getMoDataByBatchBo(@Param("boList") List<SkuAndWarehouseBatchBo> boList);

}
