package com.hete.supply.scm.server.scm.process.dao;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.RepairOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderResultExportVo;
import com.hete.supply.scm.server.scm.process.entity.dto.RepairOrderNoPageDto;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderPo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderPrintBatchCodeVo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 返修单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-29
 */
@Component
@Validated
public class RepairOrderDao extends BaseDao<RepairOrderMapper, RepairOrderPo> {

    /**
     * 根据返修单状态列表查询返修单信息
     *
     * @param statuses 返修单状态列表，不能为空
     * @return 符合条件的返修单列表，如果状态列表为空或查询结果为空，则返回空列表
     */
    public List<RepairOrderPo> listByStatus(List<RepairOrderStatus> statuses) {
        if (CollectionUtils.isEmpty(statuses)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<RepairOrderPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RepairOrderPo::getRepairOrderNo, RepairOrderPo::getPlanNo);
        queryWrapper.in(RepairOrderPo::getRepairOrderStatus, statuses);

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据返修单号查询返修单信息
     *
     * @param repairOrderNo 返修单号
     * @return 返修单信息，找不到则返回null
     */
    public RepairOrderPo getByRepairOrderNo(String repairOrderNo) {
        if (StrUtil.isEmpty(repairOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<RepairOrderPo>lambdaQuery()
                .eq(RepairOrderPo::getRepairOrderNo, repairOrderNo));
    }

    public RepairOrderPo getByRepairOrderNo(String repairOrderNo,
                                            Integer version) {
        if (StrUtil.isEmpty(repairOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<RepairOrderPo>lambdaQuery()
                .eq(RepairOrderPo::getRepairOrderNo, repairOrderNo)
                .eq(RepairOrderPo::getVersion, version));
    }

    public RepairOrderPo getByReceiveOrderNo(String receiveOrderNo) {
        if (StrUtil.isEmpty(receiveOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<RepairOrderPo>lambdaQuery()
                .eq(RepairOrderPo::getReceiveOrderNo, receiveOrderNo));
    }

    public CommonPageResult.PageInfo<RepairOrderSearchVo> searchRepairOrder(Page<Void> page,
                                                                            RepairOrderSearchDto dto) {
        IPage<RepairOrderSearchVo> pageResult = baseMapper.searchRepairOrder(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<RepairOrderPo> listByRepairOrderNos(Collection<String> repairOrderNoList) {
        return CollectionUtil.isEmpty(repairOrderNoList) ?
                Collections.emptyList() :
                list(Wrappers.<RepairOrderPo>lambdaQuery()
                        .in(RepairOrderPo::getRepairOrderNo, repairOrderNoList));
    }

    public Integer getExportRepairOrderTotals(RepairOrderSearchDto dto) {
        return baseMapper.getExportRepairOrderTotals(dto);
    }

    public Integer getExportRepairOrderResultTotals(RepairOrderSearchDto dto) {
        return baseMapper.getExportRepairOrderResultTotals(dto);
    }

    public CommonPageResult.PageInfo<RepairOrderResultExportVo> getExportRepairOrderResultList(Page<Void> page,
                                                                                               RepairOrderSearchDto dto) {
        IPage<RepairOrderResultExportVo> pageResult = baseMapper.getExportRepairOrderResultList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }


    public List<RepairOrderPo> listByPlanNo(String planNo) {
        if (StrUtil.isBlank(planNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<RepairOrderPo>lambdaQuery()
                .eq(RepairOrderPo::getPlanNo, planNo));
    }

    public List<RepairOrderPo> getListByPlanNoList(List<String> planNoList) {
        if (CollectionUtil.isEmpty(planNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<RepairOrderPo>lambdaQuery()
                .in(RepairOrderPo::getPlanNo, planNoList));
    }

    public CommonPageResult.PageInfo<RepairOrderPrintBatchCodeVo> getRepairOrderPrintBatchCode(Page<Void> page, RepairOrderNoPageDto dto) {
        IPage<RepairOrderPrintBatchCodeVo> pageResult = baseMapper.getRepairOrderPrintBatchCode(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }
}
