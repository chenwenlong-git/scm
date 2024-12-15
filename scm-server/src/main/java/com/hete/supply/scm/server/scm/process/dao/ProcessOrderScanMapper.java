package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderScanQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanExportVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanMonthStatisticsExportVo;
import com.hete.supply.scm.server.scm.process.entity.bo.ScanRecordDataStatisticsBo;
import com.hete.supply.scm.server.scm.process.entity.dto.H5WorkbenchPageDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderScanQueryDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderScanStatListDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderScanStatNumDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.H5WorkbenchVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanStatListVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 工序扫码单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessOrderScanMapper extends BaseDataMapper<ProcessOrderScanPo> {

    /**
     * 分页查询
     *
     * @param page
     * @param processOrderScanQueryDto
     * @return
     */
    IPage<ProcessOrderScanVo> getByPage(Page<Void> page, @Param("params") ProcessOrderScanQueryDto processOrderScanQueryDto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("params") ProcessOrderScanQueryByApiDto dto);

    /**
     * 统计导出的列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ProcessOrderScanExportVo> getExportList(Page<Void> page, @Param("params") ProcessOrderScanQueryByApiDto dto);

    /**
     * 统计提成以及数量
     *
     * @param dto
     * @return
     */
    ScanRecordDataStatisticsBo statNumByMonth(@Param("params") ProcessOrderScanStatNumDto dto);

    /**
     * 统计数量
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ProcessOrderScanStatListVo> statList(Page<Void> page,
                                               @Param("params") ProcessOrderScanStatListDto dto);

    Integer getMonthScanStaticCount(@Param("params") ProcessOrderScanQueryByApiDto queryDto);

    IPage<ProcessOrderScanMonthStatisticsExportVo> getMonthStatisticsExportList(Page<Void> page,
                                                                                @Param("sqlColumn") String sqlColumn,
                                                                                @Param("params") ProcessOrderScanQueryByApiDto queryDto);

    List<Long> statScanIdsNumByMonth(@Param("params") ProcessOrderScanStatNumDto processOrderScanStatNumDto);

    /**
     * 计算符合条件的扫码记录正品数量总和。
     *
     * @param processCode       处理代码，用于筛选商品。
     * @param completeUser      完成者用户名，用于筛选完成者。
     * @param completeTimeBegin 完成时间的起始范围。
     * @param completeTimeEnd   完成时间的结束范围。
     * @return 符合条件的正品商品数量总和。
     */
    Integer sumQualityGoodsCnt(String processCode,
                               String completeUser,
                               LocalDateTime completeTimeBegin,
                               LocalDateTime completeTimeEnd);

    List<ProcessOrderScanPo> listByLabelsAndTime(LocalDateTime startUtc,
                                                 LocalDateTime endUtc,
                                                 List<ProcessLabel> calProcessLabels);

    int countDistinctCompleteUser(LocalDateTime startUtc,
                                  LocalDateTime endUtc);

    IPage<H5WorkbenchVo> selectWorkbenchPage(Page<Object> page, String userKey, H5WorkbenchPageDto dto);
}
