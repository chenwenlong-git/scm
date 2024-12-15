package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleListBySkuAndDevTypeVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderChangeSearchVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SamplePurchaseSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 样品需求子单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Mapper
interface SampleChildOrderMapper extends BaseDataMapper<SampleChildOrderPo> {
    /**
     * 搜索样品采购列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<SamplePurchaseSearchVo> searchSamplePurchase(Page<Void> page, @Param("dto") SamplePurchaseSearchDto dto);

    List<SampleChildOrderChangeSearchVo> sampleChildOrderChangeSearch(String supplierCode,
                                                                      LocalDateTime sampleTime,
                                                                      LocalDateTime sampleTimeStart,
                                                                      LocalDateTime sampleTimeEnd,
                                                                      SampleOrderStatus sampleOrderStatus);

    /**
     * 获取样品单所有sku
     *
     * @return
     */
    List<String> getAllSkuList();

    Integer getSampleChildExportTotals(@Param("dto") SamplePurchaseSearchDto dto);

    IPage<SampleChildExportVo> getSampleChildExportList(Page<Void> page, @Param("dto") SamplePurchaseSearchDto dto);

    List<String> getDefectiveSampleByNo(@Param("sampleChildOrderNo") String sampleChildOrderNo,
                                        @Param("sampleOrderStatus") SampleOrderStatus sampleOrderStatus,
                                        @Param("sampleResult") SampleResult sampleResult);

    Set<String> getDistinctNoListBySkuList(@Param("skuList") List<String> skuList);

    SampleChildOrderPo getOneBySku(@Param("sku") String sku);

    List<SampleListBySkuAndDevTypeVo> getListBySkuAndDevType(@Param("skuList") List<String> skuList,
                                                             @Param("sampleDevTypeList") List<SampleDevType> sampleDevTypeList,
                                                             @Param("sampleOrderStatusList") List<SampleOrderStatus> sampleOrderStatusList,
                                                             @Param("sampleProduceLabelList") List<SampleProduceLabel> sampleProduceLabelList);

}
