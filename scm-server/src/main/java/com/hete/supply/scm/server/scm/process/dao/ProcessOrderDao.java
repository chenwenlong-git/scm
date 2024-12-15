package com.hete.supply.scm.server.scm.process.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.InventoryShortageReportExportDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.MissingInformation;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderExportByItemVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderExportByMaterialVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderExportByOrderVo;
import com.hete.supply.scm.server.scm.entity.bo.OrdersToProcessPlanParametersBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderQueryBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessPlanOrderBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 加工单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderDao extends BaseDao<ProcessOrderMapper, ProcessOrderPo> {


    /**
     * 分页查询
     *
     * @param page
     * @param processOrderQueryDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderVo> getByPage(Page<Void> page,
                                                               ProcessOrderQueryDto processOrderQueryDto,
                                                               ProcessOrderQueryBo processOrderQueryBo,
                                                               ProcessOrderStatus processOrderStatus) {
        IPage<ProcessOrderVo> pageResult
                = baseMapper.getByPage(page, processOrderQueryDto, processOrderQueryBo, processOrderStatus);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 查询总数（按单查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    public Integer getExportTotalsByOrder(ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                          ProcessOrderQueryBo processOrderQueryBo,
                                          ProcessOrderStatus processOrderStatus) {
        return baseMapper.getExportTotalsByOrder(processOrderQueryByApiDto, processOrderQueryBo, processOrderStatus);
    }

    /**
     * 查询列表（按单查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderExportByOrderVo> getExportListByOrder(Page<Void> page,
                                                                                       ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                                                                       ProcessOrderQueryBo processOrderQueryBo,
                                                                                       ProcessOrderStatus processOrderStatus) {
        IPage<ProcessOrderExportByOrderVo> pageResult
                = baseMapper.getExportListByOrder(page, processOrderQueryByApiDto, processOrderQueryBo,
                processOrderStatus);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 查询总数（按sku查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    public Integer getExportTotalsByItem(ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                         ProcessOrderQueryBo processOrderQueryBo,
                                         ProcessOrderStatus processOrderStatus) {
        return baseMapper.getExportTotalsByItem(processOrderQueryByApiDto, processOrderQueryBo, processOrderStatus);
    }

    /**
     * 查询列表（按sku查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderExportByItemVo> getExportListByItem(Page<Void> page,
                                                                                     ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                                                                     ProcessOrderQueryBo processOrderQueryBo,
                                                                                     ProcessOrderStatus processOrderStatus) {

        IPage<ProcessOrderExportByItemVo> pageResult
                = baseMapper.getExportListByItem(page, processOrderQueryByApiDto, processOrderQueryBo,
                processOrderStatus);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 查询总数（按原料查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    public Integer getExportTotalsByMaterial(ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                             ProcessOrderQueryBo processOrderQueryBo,
                                             ProcessOrderStatus processOrderStatus) {
        return baseMapper.getExportTotalsByMaterial(processOrderQueryByApiDto, processOrderQueryBo, processOrderStatus);
    }

    /**
     * @Description 按原料导出
     * @author yanjiawei
     * @Date 2024/12/3 上午10:51
     */
    public CommonPageResult.PageInfo<ProcessOrderExportByMaterialVo> getExportListByMaterial(Page<Void> page,
                                                                                             ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                                                                             ProcessOrderQueryBo processOrderQueryBo) {
        IPage<ProcessOrderExportByMaterialVo> pageResult
                = baseMapper.getExportListByMaterial(page, processOrderQueryByApiDto, processOrderQueryBo, null);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 查询加工单
     *
     * @param processOrderId
     * @return
     */
    public ProcessOrderPo getByProcessOrderId(Long processOrderId) {
        return getOne(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getProcessOrderId, processOrderId));

    }

    public ProcessOrderPo getByProcessOrderIdAndVersion(Long processOrderId,
                                                        Integer version) {
        return getOne(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getProcessOrderId, processOrderId)
                .eq(ProcessOrderPo::getVersion, version));
    }

    /**
     * 通过父节点查询返工单
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderPo> getByParentProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getParentProcessOrderNo, processOrderNo)
                .orderByDesc(ProcessOrderPo::getCreateTime));
    }

    /**
     * 通过多个父节点查询返工单
     *
     * @param processOrderNoList
     * @return
     */
    public List<ProcessOrderPo> getByParentProcessOrderNoList(List<String> processOrderNoList) {
        if (CollectionUtils.isEmpty(processOrderNoList)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .in(ProcessOrderPo::getParentProcessOrderNo, processOrderNoList)
                .orderByDesc(ProcessOrderPo::getCreateTime));

    }

    /**
     * 通过波次号查询加工单
     *
     * @param processWaveId
     * @return
     */
    public ProcessOrderPo getByProcessWaveId(Long processWaveId) {
        return getOne(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getProcessWaveId, processWaveId));

    }

    /**
     * 通过多个加工单 id 查询
     *
     * @param processOrderIds
     * @return
     */
    public List<ProcessOrderPo> getByProcessOrderIds(List<Long> processOrderIds) {
        if (CollectionUtils.isEmpty(processOrderIds)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .in(ProcessOrderPo::getProcessOrderId, processOrderIds));

    }

    /**
     * 通过单号查询加工单
     *
     * @param processOrderNo
     * @return
     */
    public ProcessOrderPo getByProcessOrderNo(String processOrderNo) {
        return StrUtil.isBlank(processOrderNo) ? null : getOne(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getProcessOrderNo,
                        processOrderNo));
    }

    /**
     * 通过多个加工单编号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderPo> getByProcessOrderNos(List<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .in(ProcessOrderPo::getProcessOrderNo, processOrderNos));

    }

    public List<ProcessOrderPo> getByProcessOrderNos(Collection<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .in(ProcessOrderPo::getProcessOrderNo, processOrderNos));

    }


    /**
     * 获取某种状态的加工单
     *
     * @param processOrderStatus
     * @return
     */
    public List<ProcessOrderPo> getByStatus(ProcessOrderStatus processOrderStatus) {
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getProcessOrderStatus, processOrderStatus))
                ;
    }

    public List<ProcessOrderPo> getByStatusList(List<ProcessOrderStatus> processOrderStatus) {
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .in(ProcessOrderPo::getProcessOrderStatus, processOrderStatus));
    }

    /**
     * 通过类型和状态查询加工单
     *
     * @param processOrderType
     * @param processOrderStatus
     * @return
     */
    public List<ProcessOrderPo> getByTypeAndStatus(ProcessOrderType processOrderType,
                                                   ProcessOrderStatus processOrderStatus) {
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .eq(ProcessOrderPo::getProcessOrderStatus, processOrderStatus)
                .eq(ProcessOrderPo::getProcessOrderType, processOrderType))
                ;
    }

    /**
     * 获取所有的加工单
     *
     * @return
     */
    public List<ProcessOrderPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }

    public List<ProcessPlanOrderBo> getOrdersToProcessPlan(OrdersToProcessPlanParametersBo param) {
        return baseMapper.getOrdersToProcessPlan(param);
    }

    public Integer getInventoryShortageReportTotals(InventoryShortageReportExportDto dto) {
        return baseMapper.getInventoryShortageReportTotals(dto);
    }

    public CommonPageResult.PageInfo<ProcessOrderPo> getInventoryShortageReportPage(InventoryShortageReportExportDto dto) {
        return page(PageDTO.of(dto.getPageNo(), dto.getPageSize()), Wrappers.<ProcessOrderPo>lambdaQuery()
                .in(ProcessOrderPo::getProcessOrderStatus, dto.getProcessOrderStatuses()));
    }

    public IPage<ProcessOrderPo> getProcessOrderNoByPage(int currentPage,
                                                         int pageSize,
                                                         Set<ProcessOrderStatus> status,
                                                         Set<ProcessOrderType> processOrderTypes) {
        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<ProcessOrderPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProcessOrderPo::getProcessOrderStatus, status);
        queryWrapper.in(ProcessOrderPo::getProcessOrderType, processOrderTypes);
        queryWrapper.select(ProcessOrderPo::getProcessOrderNo);


        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    public int countStoredNumBetweenTimes(LocalDateTime startUtc,
                                          LocalDateTime endUtc) {
        return baseMapper.countStoredNumBetweenTimes(startUtc, endUtc);
    }

    public IPage<ProcessOrderPo> selectPage(Page<ProcessOrderPo> page,
                                            List<ProcessOrderStatus> queryStatus) {
        // 构建查询条件
        LambdaQueryWrapper<ProcessOrderPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.isNotNull(ProcessOrderPo::getPromiseDateDelayed)
                .in(ProcessOrderPo::getProcessOrderStatus, queryStatus)
                .orderByAsc(ProcessOrderPo::getProcessOrderId);
        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 查询不在指定状态且创建时间在指定范围内的加工单列表
     *
     * @param startTime       创建时间开始范围
     * @param endTime         创建时间结束范围
     * @param excludeStatus   非加工单状态
     * @param processOrderNos 加工单号列表
     * @return 符合条件的加工单列表
     */
    public List<ProcessOrderPo> listByTimeRangeAndExcludeStatusAndOrderNos(LocalDateTime startTime,
                                                                           LocalDateTime endTime,
                                                                           ProcessOrderStatus excludeStatus,
                                                                           List<String> processOrderNos) {
        return list(Wrappers.<ProcessOrderPo>lambdaQuery()
                .select(ProcessOrderPo::getProcessOrderNo)
                .between(ProcessOrderPo::getCreateTime, startTime, endTime)
                .ne(ProcessOrderPo::getProcessOrderStatus, excludeStatus)
                .in(ProcessOrderPo::getProcessOrderNo, processOrderNos)
        );
    }

    public IPage<ProcessOrderPo> getDeliveryWarehouseInitData(Page<ProcessOrderPo> page,
                                                              ProcessOrderType processOrderType,
                                                              ProcessOrderStatus processOrderStatus,
                                                              List<MissingInformation> missInfoList,
                                                              String deliveryWarehouseCode,
                                                              List<String> skuList) {
        return baseMapper.getDeliveryWarehouseInitData(page, processOrderType, processOrderStatus, missInfoList, deliveryWarehouseCode, skuList);
    }
}
