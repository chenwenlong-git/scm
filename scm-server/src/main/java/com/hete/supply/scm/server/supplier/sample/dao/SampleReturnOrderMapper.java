package com.hete.supply.scm.server.supplier.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReturnExportVo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 样品退货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface SampleReturnOrderMapper extends BaseDataMapper<SampleReturnOrderPo> {

    IPage<SampleReturnVo> searchProductPurchase(Page<Void> page, @Param("dto") SampleReturnDto dto,
                                                @Param("sampleReturnOrderNoList") List<String> sampleReturnOrderNoList);

    Integer getExportTotals(@Param("dto") SampleReturnDto dto,
                            @Param("sampleReturnOrderNoList") List<String> sampleReturnOrderNoList);

    IPage<SampleReturnExportVo> getExportList(Page<Void> page, SampleReturnDto dto,
                                              @Param("sampleReturnOrderNoList") List<String> sampleReturnOrderNoList);
}
