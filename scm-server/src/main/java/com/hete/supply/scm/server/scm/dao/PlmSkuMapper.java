package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.GoodsPriceDto;
import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsPriceExportVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuProcessVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuExportVo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceSearchVo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSearchVo;
import com.hete.supply.scm.server.scm.production.entity.vo.PlmSkuSearchVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuCycleBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductComparePageVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * plm的产品信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Mapper
interface PlmSkuMapper extends BaseDataMapper<PlmSkuPo> {

    IPage<SupplierProductComparePageVo> selectSupplierProductPage(Page<Void> page, @Param("dto") SupplierProductCompareDto dto);

    IPage<ProduceDataSearchVo> selectProduceDataPage(Page<Void> page, @Param("dto") ProduceDataSearchDto dto);

    Integer getExportSkuProcessTotals(@Param("dto") ProduceDataSearchDto dto);

    IPage<ProduceDataExportSkuProcessVo> getSkuProcessExportList(Page<Void> page, @Param("dto") ProduceDataSearchDto dto);

    Integer getExportSkuAttrTotals(@Param("dto") ProduceDataSearchDto dto);

    IPage<ProduceDataExportSkuAttrVo> getSkuAttrExportList(Page<Void> page, @Param("dto") ProduceDataSearchDto dto);


    IPage<GoodsPriceSearchVo> searchGoodsPricePage(Page<Void> page, @Param("dto") GoodsPriceDto dto);

    Integer getGoodsPriceExportTotals(@Param("dto") GoodsPriceDto dto);

    IPage<GoodsPriceExportVo> getGoodsPriceExportList(Page<Void> page, @Param("dto") GoodsPriceDto dto);

    List<SkuCycleBo> listByProduceCycleBo(Collection<String> skuList);

    IPage<PlmSkuSearchVo> searchPlmSku(Page<Void> page, @Param("dto") PlmSkuSearchDto dto);

    Integer getExportTotals(@Param("dto") PlmSkuSearchDto dto);

    IPage<SkuProdSkuExportVo> getExportList(Page<Void> page, @Param("dto") PlmSkuSearchDto dto);
}
