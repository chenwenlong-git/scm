package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrItemDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrPricePageDto;
import com.hete.supply.scm.server.scm.entity.po.SkuAttrPricePo;
import com.hete.supply.scm.server.scm.entity.vo.SkuAttrPricePageVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * sku属性定价表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-09
 */
@Mapper
interface SkuAttrPriceMapper extends BaseDataMapper<SkuAttrPricePo> {

    IPage<SkuAttrPricePageVo> searchSkuAttrPricePage(Page<Void> page, @Param("dto") SkuAttrPricePageDto dto);

    List<SkuAttrPricePo> getListBySkuAndAttr(@Param("dtoList") List<? extends SkuAttrItemDto> itemDtoList);
}
