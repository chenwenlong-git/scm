package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleParentExportVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 样品需求母单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Mapper
interface SampleParentOrderMapper extends BaseDataMapper<SampleParentOrderPo> {

    /**
     * 搜索样品列表
     *
     * @param page
     * @param dto
     * @param skuParentOrderNoList
     * @return
     */
    IPage<SampleSearchVo> searchSample(Page<Void> page, @Param("dto") SampleSearchDto dto,
                                       @Param("skuParentOrderNoList") List<String> skuParentOrderNoList);


    List<String> getDefectiveSampleNoSet();

    Integer getSampleParentExportTotals(@Param("dto") SampleSearchDto dto,
                                        @Param("skuParentOrderNoList") List<String> skuParentOrderNoList);

    IPage<SampleParentExportVo> getExportList(Page<Void> page,
                                              @Param("dto") SampleSearchDto dto,
                                              @Param("skuParentOrderNoList") List<String> skuParentOrderNoList);

    void updatePurchaseCnt(@Param("id") Long id,
                           @Param("version") Integer version,
                           @Param("purchaseSum") int purchaseSum);
}
