package com.hete.supply.scm.server.scm.settle.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductOrderExportBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 扣款单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-05
 */
@Component
@Validated
public class DeductOrderDao extends BaseDao<DeductOrderMapper, DeductOrderPo> {

    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 16:14
     */
    public CommonPageResult.PageInfo<DeductOrderVo> selectDeductOrderPage(Page<Void> page, DeductOrderDto deductOrderDto) {
        IPage<DeductOrderVo> pageResult = baseMapper.selectDeductOrderPage(page, deductOrderDto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 获取导出总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(DeductOrderQueryByApiDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<DeductOrderExportVo> getExportList(Page<Void> page, DeductOrderQueryByApiDto dto) {
        IPage<DeductOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }


    /**
     * 删除
     *
     * @author ChenWenLong
     * @date 2022/11/8 17:48
     */
    public void delete(Long deductOrderId) {
        baseMapper.deleteById(deductOrderId);
    }

    /**
     * 通过供应商和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<DeductOrderPo> getBySupplierCodeAndExamineTime(List<String> supplierCodeList, LocalDateTime examineTime,
                                                               LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        List<DeductType> deductTypesList = new ArrayList<>();
        deductTypesList.add(DeductType.PRICE);
        deductTypesList.add(DeductType.QUALITY);
        deductTypesList.add(DeductType.OTHER);
        deductTypesList.add(DeductType.PAY);
        deductTypesList.add(DeductType.DEFECTIVE_RETURN);
        return list(Wrappers.<DeductOrderPo>lambdaQuery().in(CollectionUtil.isNotEmpty(supplierCodeList), DeductOrderPo::getSupplierCode, supplierCodeList)
                .le(examineTime != null, DeductOrderPo::getExamineTime, examineTime)
                .ge(examineTimeStart != null, DeductOrderPo::getExamineTime, examineTimeStart)
                .le(examineTimeEnd != null, DeductOrderPo::getExamineTime, examineTimeEnd)
                .eq(DeductOrderPo::getDeductStatus, DeductStatus.AUDITED)
                .in(DeductOrderPo::getDeductType, deductTypesList)
                .eq(DeductOrderPo::getSettleOrderNo, ""));
    }

    /**
     * 通过用户和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<DeductOrderPo> getByDeductUserAndExamineTime(String deductUser, LocalDateTime examineTime,
                                                             LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        return list(Wrappers.<DeductOrderPo>lambdaQuery().eq(StringUtils.isNotBlank(deductUser), DeductOrderPo::getDeductUser, deductUser)
                .le(examineTime != null, DeductOrderPo::getExamineTime, examineTime)
                .ge(examineTimeStart != null, DeductOrderPo::getExamineTime, examineTimeStart)
                .le(examineTimeEnd != null, DeductOrderPo::getExamineTime, examineTimeEnd)
                .eq(DeductOrderPo::getDeductStatus, DeductStatus.AUDITED)
                .eq(DeductOrderPo::getDeductType, DeductType.PROCESS)
                .eq(DeductOrderPo::getSettleOrderNo, ""));
    }

    /**
     * 通过编号查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public DeductOrderPo getByDeductOrderNo(String deductOrderNo) {
        return baseMapper.selectOne(Wrappers.<DeductOrderPo>lambdaQuery()
                .eq(DeductOrderPo::getDeductOrderNo, deductOrderNo));
    }

    /**
     * 通过批量编号查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public List<DeductOrderPo> getByDeductOrderNoList(List<String> deductOrderNoList) {
        if (CollectionUtil.isEmpty(deductOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DeductOrderPo>lambdaQuery()
                .in(DeductOrderPo::getDeductOrderNo, deductOrderNoList));
    }


    /**
     * 通过编号查询和版本号查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public DeductOrderPo getByDeductOrderNo(String deductOrderNo, Integer version) {
        return baseMapper.selectOne(Wrappers.<DeductOrderPo>lambdaQuery()
                .eq(DeductOrderPo::getDeductOrderNo, deductOrderNo)
                .eq(DeductOrderPo::getVersion, version));
    }

    /**
     * 根据编号批量修改状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 16:20
     */
    public void updateBatchDeductOrderNo(List<String> deductOrderNos, DeductStatus deductStatus) {
        if (CollectionUtil.isNotEmpty(deductOrderNos)) {
            final DeductOrderPo deductOrderPo = new DeductOrderPo();
            deductOrderPo.setDeductStatus(deductStatus);
            deductOrderPo.setPayTime(LocalDateTime.now());
            baseMapper.update(deductOrderPo, Wrappers.<DeductOrderPo>lambdaUpdate()
                    .in(DeductOrderPo::getDeductOrderNo, deductOrderNos));
        }
    }

    public List<DeductOrderExportBo> getPurchaseBatchDeductOrderNo(List<String> deductOrderNoList) {
        if (CollectionUtil.isEmpty(deductOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getPurchaseBatchDeductOrderNo(deductOrderNoList);
    }

    public List<DeductOrderExportBo> getQualityBatchDeductOrderNo(List<String> deductOrderNoList) {
        if (CollectionUtil.isEmpty(deductOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getQualityBatchDeductOrderNo(deductOrderNoList);
    }

    /**
     * 通过扣款员工和类型获取列表
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public List<DeductOrderPo> getByDeductUserAndType(@NotBlank String deductUser, DeductType deductType) {
        return list(Wrappers.<DeductOrderPo>lambdaQuery()
                .eq(DeductOrderPo::getDeductUser, deductUser)
                .eq(deductType != null, DeductOrderPo::getDeductType, deductType));
    }

    /**
     * 通过审核时间和结算单查询数据列表
     *
     * @param examineTimeStart:
     * @param examineTimeEnd:
     * @param settleOrderNoList:
     * @param deductTypeList:
     * @return List<DeductOrderPo>
     * @author ChenWenLong
     * @date 2023/6/7 15:43
     */
    public List<DeductOrderPo> getByExamineTimeAndSettleOrderNo(LocalDateTime examineTimeStart,
                                                                LocalDateTime examineTimeEnd,
                                                                List<String> settleOrderNoList,
                                                                List<DeductType> deductTypeList,
                                                                List<String> supplierCodeList,
                                                                List<String> deductUserList) {
        return list(Wrappers.<DeductOrderPo>lambdaQuery()
                .and(w -> w.and(w1 -> w1.ge(examineTimeStart != null, DeductOrderPo::getExamineTime, examineTimeStart)
                                .le(examineTimeEnd != null, DeductOrderPo::getExamineTime, examineTimeEnd))
                        .or(w2 -> w2.in(CollectionUtil.isNotEmpty(settleOrderNoList), DeductOrderPo::getSettleOrderNo, settleOrderNoList)))
                .eq(DeductOrderPo::getDeductStatus, DeductStatus.AUDITED)
                .in(CollectionUtil.isNotEmpty(deductTypeList), DeductOrderPo::getDeductType, deductTypeList)
                .in(CollectionUtil.isNotEmpty(supplierCodeList), DeductOrderPo::getSupplierCode, supplierCodeList)
                .in(CollectionUtil.isNotEmpty(deductUserList), DeductOrderPo::getDeductUser, deductUserList)
        );
    }

    /**
     * 获取sku导出总数
     *
     * @param dto
     * @return
     */
    public Integer getExportSkuTotals(DeductOrderDto dto) {
        return baseMapper.getExportSkuTotals(dto);
    }

    /**
     * 查询sku导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<DeductOrderPo> getExportSkuList(Page<Void> page, DeductOrderDto dto) {
        IPage<DeductOrderPo> pageResult = baseMapper.getExportSkuList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }
}
