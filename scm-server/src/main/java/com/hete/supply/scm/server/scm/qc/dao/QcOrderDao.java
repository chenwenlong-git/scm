package com.hete.supply.scm.server.scm.qc.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.QcDto;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.api.scm.entity.vo.QcDetailSkuVo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.vo.QcSearchVo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 质检单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Component
@Validated
public class QcOrderDao extends BaseDao<QcOrderMapper, QcOrderPo> {

    /**
     * 根据加工订单号查询相关的质检单列表。
     *
     * @param processOrderNo 加工订单号，用于筛选相关的质检单。
     * @return 包含相关质检单信息的列表。
     */
    public List<QcOrderPo> listByProcessOrderNo(@NotBlank String processOrderNo) {
        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.<QcOrderPo>lambdaQuery()
                .eq(QcOrderPo::getProcessOrderNo, processOrderNo);

        // 执行查询并返回结果列表
        return list(queryWrapper);
    }

    /**
     * 根据质检单号获取对应的质检单信息。
     *
     * @param qcOrderNo 质检单号，用于唯一标识质检单。
     * @return 对应质检单号的质检单信息，如果未找到则返回空对象。
     */
    public QcOrderPo getByQcOrderNo(String qcOrderNo) {
        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.<QcOrderPo>lambdaQuery()
                .eq(QcOrderPo::getQcOrderNo, qcOrderNo);

        // 执行查询并返回单个质检单信息，如果未找到则返回空对象
        return getOne(queryWrapper);
    }

    /**
     * 根据一组质检单号列表，查询相应的质检单信息。
     *
     * @param qcOrderNoList 一组质检单号，用于检索对应的质检单信息。
     * @return 包含质检单信息的列表。如果未找到匹配的质检单则返回空列表。
     */
    public List<QcOrderPo> getByQcOrderNos(Collection<String> qcOrderNoList) {
        // 判断输入列表是否为空，如果为空则返回空列表
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return new ArrayList<>();
        }

        // 使用LambdaQueryWrapper创建查询条件，通过in方法匹配一组质检单号
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.<QcOrderPo>lambdaQuery()
                .in(QcOrderPo::getQcOrderNo, qcOrderNoList);

        // 执行查询并返回质检单信息列表，如果未找到匹配的质检单则返回空列表
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据一组收货单号列表，查询相应的质检单信息。
     *
     * @param receiveNos 一组收货单号，用于检索对应的质检单信息。
     * @return 包含质检单信息的列表。如果未找到匹配的质检单则返回空列表。
     */
    public List<QcOrderPo> getByReceiveNos(Collection<String> receiveNos) {
        // 判断输入列表是否为空，如果为空则返回空列表
        if (CollectionUtils.isEmpty(receiveNos)) {
            return Collections.emptyList();
        }

        // 使用LambdaQueryWrapper创建查询条件，通过in方法匹配一组收货单号
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.<QcOrderPo>lambdaQuery()
                .in(QcOrderPo::getReceiveOrderNo, receiveNos);

        // 执行查询并返回质检单信息列表，如果未找到匹配的质检单则返回空列表
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 分页获取在指定日期范围内并且具有指定状态的质检单列表。
     *
     * @param beforeDay   查询N天前的数据
     * @param qcState     质检单状态
     * @param currentPage 当前页数
     * @param pageSize    每页数据条数
     * @return 包含质检单信息的分页对象
     */
    public IPage<QcOrderPo> getQcOrdersWithInLastDaysAndState(int beforeDay,
                                                              QcState qcState,
                                                              int currentPage,
                                                              int pageSize) {
        // 获取当前日期
        LocalDate endDate = LocalDate.now();
        // 计算 N 天前的日期
        LocalDate startDate = endDate.minusDays(beforeDay);

        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.lambdaQuery();
        // 添加时间范围条件：更新时间在 startDate 和 endDate 之间
        queryWrapper.between(QcOrderPo::getUpdateTime, startDate, endDate);
        // 添加质检单状态条件
        queryWrapper.eq(QcOrderPo::getQcState, qcState);

        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    /**
     * 分页获取在指定日期范围内并且具有指定状态的质检单列表，且 processOrderNo 为空。
     *
     * @param beforeDay   查询N天前的数据
     * @param qcState     质检单状态
     * @param currentPage 当前页数
     * @param pageSize    每页数据条数
     * @return 包含质检单信息的分页对象
     */
    public IPage<QcOrderPo> getQcOrdersWithInLastDaysAndStateAndFilter(int beforeDay,
                                                                       QcState qcState,
                                                                       int currentPage,
                                                                       int pageSize) {
        // 获取当前日期
        LocalDate endDate = LocalDate.now();
        // 计算 N 天前的日期
        LocalDate startDate = endDate.minusDays(beforeDay);

        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.lambdaQuery();
        // 添加时间范围条件：更新时间在 startDate 和 endDate 之间
        queryWrapper.between(QcOrderPo::getUpdateTime, startDate, endDate);
        // 添加质检单状态条件
        queryWrapper.eq(QcOrderPo::getQcState, qcState);
        // 添加 processOrderNo 为空的条件
        queryWrapper.and(i -> i.isNull(QcOrderPo::getProcessOrderNo)
                .or()
                .eq(QcOrderPo::getProcessOrderNo, ""));
        // 添加 repairOrderNo 为空的条件
        queryWrapper.and(i -> i.isNull(QcOrderPo::getRepairOrderNo)
                .or()
                .eq(QcOrderPo::getRepairOrderNo, ""));

        queryWrapper.ne(QcOrderPo::getQcOrigin, QcOrigin.OUTBOUND);

        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    /**
     * 分页获取在指定日期范围内并且具有指定状态的质检单列表，且 processOrderNo 不为空。
     *
     * @param beforeDay   查询N天前的数据
     * @param qcState     质检单状态
     * @param currentPage 当前页数
     * @param pageSize    每页数据条数
     * @return 包含质检单信息的分页对象
     */
    public IPage<QcOrderPo> getQcOrdersWithInLastDaysAndStateAndProcessOrderNoNotBlank(int beforeDay,
                                                                                       QcState qcState,
                                                                                       int currentPage,
                                                                                       int pageSize) {
        // 获取当前日期
        LocalDate endDate = LocalDate.now();
        // 计算 N 天前的日期
        LocalDate startDate = endDate.minusDays(beforeDay);

        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<QcOrderPo> queryWrapper = Wrappers.lambdaQuery();
        // 添加时间范围条件：更新时间在 startDate 和 endDate 之间
        queryWrapper.between(QcOrderPo::getUpdateTime, startDate, endDate);
        // 添加质检单状态条件
        queryWrapper.eq(QcOrderPo::getQcState, qcState);
        // 添加 processOrderNo 不为空的条件
        queryWrapper.and(i -> i.isNotNull(QcOrderPo::getProcessOrderNo)
                .ne(QcOrderPo::getProcessOrderNo, ""));

        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    public IPage<QcSearchVo> searchQcOrderPage(Page<Void> page, QcSearchDto dto) {
        return baseMapper.searchQcOrderPage(page, dto);
    }

    public List<QcOrderPo> getListByRepairOrderNoList(List<String> repairOrderNoList) {
        if (CollectionUtils.isEmpty(repairOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<QcOrderPo>lambdaQuery()
                .in(QcOrderPo::getRepairOrderNo, repairOrderNoList));

    }

    public QcOrderPo getOneByRepairOrderNo(String repairOrderNo) {
        if (StringUtils.isBlank(repairOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<QcOrderPo>lambdaQuery()
                .eq(QcOrderPo::getRepairOrderNo, repairOrderNo));
    }

    public IPage<QcOrderPo> getProcAndRepairQcNoByPage(int currentPage,
                                                       int pageSize) {

        return baseMapper.getProcAndRepairQcNoByPage(PageDTO.of(currentPage, pageSize), ReceiveType.PROCESS_PRODUCT);
    }

    public void updateByQcOrderNoList(QcOrderPo qcOrderPo, List<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return;
        }
        baseMapper.update(qcOrderPo, Wrappers.<QcOrderPo>lambdaUpdate()
                .in(QcOrderPo::getQcOrderNo, qcOrderNoList));
    }

    public List<QcDetailSkuVo> listBySkuAndQcState(QcDto dto) {
        return baseMapper.listBySkuAndQcState(dto.getSkuCodeList(), dto.getQcStateList(), dto.getWarehouseCodeList());
    }
}
