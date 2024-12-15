package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReceiptExportVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 样品收货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Mapper
interface SampleReceiptOrderMapper extends BaseDataMapper<SampleReceiptOrderPo> {

    IPage<SampleReceiptSearchVo> searchSampleReceipt(Page<Void> page, @Param("dto") SampleReceiptSearchDto dto,
                                                     @Param("sampleReceiptOrderNoList") List<String> sampleReceiptOrderNoList);

    Integer getExportTotals(@Param("dto") SampleReceiptSearchDto dto,
                            @Param("sampleReceiptOrderNoList") List<String> sampleReceiptOrderNoList);

    IPage<SampleReceiptExportVo> getExportList(Page<Void> page, @Param("dto") SampleReceiptSearchDto dto,
                                               @Param("sampleReceiptOrderNoList") List<String> sampleReceiptOrderNoList);
}
