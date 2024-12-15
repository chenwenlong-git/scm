package com.hete.supply.scm.server.scm.process.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
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
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 工序扫码单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderScanDao extends BaseDao<ProcessOrderScanMapper, ProcessOrderScanPo> {


    public CommonPageResult.PageInfo<ProcessOrderScanVo> getByPage(ProcessOrderScanQueryDto queryDto) {
        IPage<ProcessOrderScanVo> pageResult = baseMapper.getByPage(PageDTO.of(queryDto.getPageNo(), queryDto.getPageSize()), queryDto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 统计提成
     *
     * @param page
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderScanStatListVo> statList(Page<Void> page,
                                                                          ProcessOrderScanStatListDto dto) {
        IPage<ProcessOrderScanStatListVo> pageResult = baseMapper.statList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 统计导出的总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(ProcessOrderScanQueryByApiDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 统计提成以及数量
     *
     * @param dto
     * @return
     */
    public ScanRecordDataStatisticsBo statNumByMonth(ProcessOrderScanStatNumDto dto) {
        return baseMapper.statNumByMonth(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderScanExportVo> getExportList(Page<Void> page,
                                                                             ProcessOrderScanQueryByApiDto dto) {
        IPage<ProcessOrderScanExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * @param processOrderProcedureId
     * @return
     */
    public ProcessOrderScanPo getByProcessOrderProcedureId(Long processOrderProcedureId) {
        return getOne(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .eq(ProcessOrderScanPo::getProcessOrderProcedureId, processOrderProcedureId)
        );
    }

    /**
     * 通过通过工序
     *
     * @param processOrderProcedureIds
     * @return
     */
    public List<ProcessOrderScanPo> getByProcessOrderProcedureIds(List<Long> processOrderProcedureIds) {
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .in(ProcessOrderScanPo::getProcessOrderProcedureId, processOrderProcedureIds)
        );
    }

    public List<ProcessOrderScanPo> getByProcessProcessCodes(List<String> processCodes) {
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .in(ProcessOrderScanPo::getProcessCode, processCodes)
        );
    }

    public List<ProcessOrderScanPo> listByProcessCode(String processCode) {
        if (StrUtil.isBlank(processCode)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .eq(ProcessOrderScanPo::getProcessCode, processCode)
        );
    }

    /**
     * 获取加工单编号获取扫码记录
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderScanPo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .eq(ProcessOrderScanPo::getProcessOrderNo, processOrderNo)
        );
    }

    /**
     * 通过加工单号查询扫码记录
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderScanPo> getByProcessOrderNos(List<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .in(ProcessOrderScanPo::getProcessOrderNo, processOrderNos)
        );
    }

    /**
     * 通过多个主键查询
     *
     * @param processOrderScanIds
     * @return
     */
    public List<ProcessOrderScanPo> getByIds(Collection<Long> processOrderScanIds) {
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .in(ProcessOrderScanPo::getProcessOrderScanId, processOrderScanIds)
        );
    }

    /**
     * 通过扫码人和时间获取某个月的信息
     *
     * @author ChenWenLong
     * @date 2022/11/10 18:10
     */
    public List<ProcessOrderScanPo> getByCompleteUserAndCompleteTime(Long completeUser,
                                                                     LocalDateTime completeTime) {
        // 获取当前月的第一天
        LocalDate firstDay = completeTime.toLocalDate()
                .with(TemporalAdjusters.firstDayOfMonth());
        // 当前月份加1
        LocalDate plusMonths = completeTime.toLocalDate()
                .plusMonths(1);
        // 获取下个月的第一天
        LocalDate nextMonthFirstDay = plusMonths.with(TemporalAdjusters.firstDayOfMonth());
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .eq(ProcessOrderScanPo::getCompleteUser, completeUser)
                .ge(ProcessOrderScanPo::getCreateTime, firstDay)
                .lt(ProcessOrderScanPo::getCreateTime, nextMonthFirstDay));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    /**
     * 通过用户编码和扫码时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<ProcessOrderScanPo> getByCompleteUserAndTime(String completeUser,
                                                             LocalDateTime completeTime,
                                                             LocalDateTime completeTimeStart,
                                                             LocalDateTime completeTimeEnd,
                                                             String systemUser) {
        return list(Wrappers.<ProcessOrderScanPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(completeUser), ProcessOrderScanPo::getCompleteUser, completeUser)
                .le(completeTime != null, ProcessOrderScanPo::getCompleteTime, completeTime)
                .ge(completeTimeStart != null, ProcessOrderScanPo::getCompleteTime, completeTimeStart)
                .le(completeTimeEnd != null, ProcessOrderScanPo::getCompleteTime, completeTimeEnd)
                .ne(ProcessOrderScanPo::getCompleteUser, systemUser)
                .ne(ProcessOrderScanPo::getCompleteUser, "")
                .isNotNull(ProcessOrderScanPo::getCompleteUser)
                .orderByDesc(ProcessOrderScanPo::getCompleteTime));
    }

    /**
     * @return
     */
    public List<ProcessOrderScanPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }

    public List<ProcessOrderScanPo> getByRelateUserKey(String userKey,
                                                       LocalDateTime scanTime) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(ProcessOrderScanPo::getCreateTime, scanTime.withHour(0)
                                .withMinute(0)
                                .withSecond(0),
                        scanTime.withHour(23)
                                .withMinute(59)
                                .withSecond(59))
                .and(wrapper -> wrapper.eq(ProcessOrderScanPo::getReceiptUser, userKey)
                        .or()
                        .eq(ProcessOrderScanPo::getProcessingUser, userKey)
                        .or()
                        .eq(ProcessOrderScanPo::getCompleteUser, userKey));

        return baseMapper.selectList(queryWrapper);
    }

    public Integer getMonthScanStaticCount(ProcessOrderScanQueryByApiDto queryDto) {
        return baseMapper.getMonthScanStaticCount(queryDto);
    }


    public IPage<ProcessOrderScanMonthStatisticsExportVo> getMonthStatisticsExportList(Page<Void> page,
                                                                                       String sqlColumn,
                                                                                       ProcessOrderScanQueryByApiDto queryDto) {
        return baseMapper.getMonthStatisticsExportList(page, sqlColumn, queryDto);
    }

    /**
     * 根据工序编码、完成人、完成时间范围统计正品数量总和。
     *
     * @param processCode       工序编码。
     * @param completeUser      完成人。
     * @param completeTimeBegin 完成时间范围的起始时间。
     * @param completeTimeEnd   完成时间范围的结束时间。
     * @return 正品数量总和。
     */
    public int sumQualityGoodsCnt(String processCode,
                                  String completeUser,
                                  LocalDateTime completeTimeBegin,
                                  LocalDateTime completeTimeEnd) {
        Integer result = baseMapper.sumQualityGoodsCnt(processCode, completeUser, completeTimeBegin, completeTimeEnd);
        return Objects.isNull(result) ? 0 : result;
    }

    public List<Long> statScanIdsNumByMonth(ProcessOrderScanStatNumDto processOrderScanStatNumDto) {
        return baseMapper.statScanIdsNumByMonth(processOrderScanStatNumDto);
    }

    /**
     * 根据完成时间范围查询不重复的用户和工序编码，按用户名升序排序。
     *
     * @param completeTimeBegin 完成时间起始值
     * @param completeTimeEnd   完成时间结束值
     * @return 不重复的用户和工序编码列表，按用户名升序排序
     */
    public List<ProcessOrderScanPo> getDistinctUserAndProcessCodeSortedByUser(
            LocalDateTime completeTimeBegin,
            LocalDateTime completeTimeEnd) {
        // 构建 LambdaQueryWrapper 条件
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProcessOrderScanPo::getCompleteUser, ProcessOrderScanPo::getProcessCode)
                .groupBy(ProcessOrderScanPo::getCompleteUser, ProcessOrderScanPo::getProcessCode)
                .between(ProcessOrderScanPo::getCompleteTime, completeTimeBegin, completeTimeEnd)
                .orderByAsc(ProcessOrderScanPo::getCompleteUser);
        // 执行查询
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据工序编码、完成人和完成时间范围获取工序扫码信息列表。
     *
     * @param processCode       工序编码。
     * @param completeUser      完成人。
     * @param completeBeginTime 完成时间范围的起始时间。
     * @param completeEndTime   完成时间范围的结束时间。
     * @return 符合条件的工序扫码信息列表。
     */
    public List<ProcessOrderScanPo> getProcessOrderScanList(String processCode,
                                                            String completeUser,
                                                            LocalDateTime completeBeginTime,
                                                            LocalDateTime completeEndTime) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessOrderScanPo::getProcessCode, processCode)
                .eq(ProcessOrderScanPo::getCompleteUser, completeUser)
                .ge(completeBeginTime != null, ProcessOrderScanPo::getCompleteTime, completeBeginTime)
                .le(completeEndTime != null, ProcessOrderScanPo::getCompleteTime, completeEndTime)
                .orderByAsc(ProcessOrderScanPo::getCompleteTime);

        return getBaseMapper().selectList(queryWrapper);
    }

    public List<ProcessOrderScanPo> listByProcessCodeAndCompleteTime(String processCode,
                                                                     LocalDateTime completeTimeBegin,
                                                                     LocalDateTime completeTimeEnd) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessOrderScanPo::getProcessCode, processCode)
                .ge(completeTimeBegin != null, ProcessOrderScanPo::getCompleteTime, completeTimeBegin)
                .le(completeTimeEnd != null, ProcessOrderScanPo::getCompleteTime, completeTimeEnd)
                .orderByAsc(ProcessOrderScanPo::getCompleteTime);

        return getBaseMapper().selectList(queryWrapper);
    }

    public List<ProcessOrderScanPo> listByCompleteTime(LocalDateTime completeTimeBegin,
                                                       LocalDateTime completeTimeEnd) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(completeTimeBegin != null, ProcessOrderScanPo::getCompleteTime, completeTimeBegin)
                .le(completeTimeEnd != null, ProcessOrderScanPo::getCompleteTime, completeTimeEnd)
                .orderByAsc(ProcessOrderScanPo::getCompleteTime);

        return getBaseMapper().selectList(queryWrapper);
    }

    /**
     * 根据工序编码列表、完成人和完成时间范围获取工序扫码信息列表。
     *
     * @param processCodes      工序编码列表。
     * @param completeUser      完成人。
     * @param completeBeginTime 完成时间范围的起始时间。
     * @param completeEndTime   完成时间范围的结束时间。
     * @return 符合条件的工序扫码信息列表。
     */
    public List<ProcessOrderScanPo> getProcessOrderScanList(Collection<String> processCodes,
                                                            String completeUser,
                                                            LocalDateTime completeBeginTime,
                                                            LocalDateTime completeEndTime) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProcessOrderScanPo::getProcessCode, processCodes)
                .eq(ProcessOrderScanPo::getCompleteUser, completeUser)
                .ge(completeBeginTime != null, ProcessOrderScanPo::getCompleteTime, completeBeginTime)
                .le(completeEndTime != null, ProcessOrderScanPo::getCompleteTime, completeEndTime)
                .orderByAsc(ProcessOrderScanPo::getCompleteTime);

        return getBaseMapper().selectList(queryWrapper);
    }

    public List<String> listProcessCodesByCompleteTime(LocalDateTime completeTimeBegin,
                                                       LocalDateTime completeTimeEnd) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProcessOrderScanPo::getProcessCode)
                .ge(completeTimeBegin != null, ProcessOrderScanPo::getCompleteTime, completeTimeBegin)
                .le(completeTimeEnd != null, ProcessOrderScanPo::getCompleteTime, completeTimeEnd)
                .groupBy(ProcessOrderScanPo::getProcessCode);

        List<ProcessOrderScanPo> processOrderScanPos = baseMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(processOrderScanPos) ? Collections.emptyList() :
                processOrderScanPos.stream()
                        .map(ProcessOrderScanPo::getProcessCode)
                        .collect(Collectors.toList());
    }

    public IPage<ProcessOrderScanPo> listByCompleteTimeWithPage(int pageNum,
                                                                int pageSize,
                                                                LocalDateTime completeTimeBegin,
                                                                LocalDateTime completeTimeEnd) {
        LambdaQueryWrapper<ProcessOrderScanPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(completeTimeBegin != null, ProcessOrderScanPo::getCompleteTime, completeTimeBegin)
                .le(completeTimeEnd != null, ProcessOrderScanPo::getCompleteTime, completeTimeEnd)
                .orderByAsc(ProcessOrderScanPo::getProcessOrderScanId);

        Page<ProcessOrderScanPo> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }

    public List<ProcessOrderScanPo> listByLabelsAndTime(LocalDateTime startUtc,
                                                        LocalDateTime endUtc,
                                                        List<ProcessLabel> calProcessLabels) {
        return baseMapper.listByLabelsAndTime(startUtc, endUtc, calProcessLabels);
    }

    public int countDistinctCompleteUser(LocalDateTime startUtc,
                                         LocalDateTime endUtc) {
        return baseMapper.countDistinctCompleteUser(startUtc, endUtc);
    }

    public IPage<H5WorkbenchVo> selectWorkbenchPage(String userKey, H5WorkbenchPageDto dto) {
        return baseMapper.selectWorkbenchPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), userKey, dto);
    }
}
