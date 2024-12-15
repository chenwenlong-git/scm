package com.hete.supply.scm.server.scm.settle.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.bo.SupplementOrderPurchaseExportBo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
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
 * 补款单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SupplementOrderDao extends BaseDao<SupplementOrderMapper, SupplementOrderPo> {

    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 16:14
     */
    public CommonPageResult.PageInfo<SupplementOrderVo> selectSupplementOrderPage(Page<Void> page, SupplementOrderDto supplementOrderDto) {
        IPage<SupplementOrderVo> pageResult = baseMapper.selectSupplementOrderPage(page, supplementOrderDto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 获取导出总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(SupplementOrderQueryByApiDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<SupplementOrderExportVo> getExportList(Page<Void> page, SupplementOrderQueryByApiDto dto) {
        IPage<SupplementOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过供应商和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<SupplementOrderPo> getBySupplierCodeAndExamineTime(List<String> supplierCodeList, LocalDateTime examineTime,
                                                                   LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        List<SupplementType> supplementTypeList = new ArrayList<>();
        supplementTypeList.add(SupplementType.PRICE);
        supplementTypeList.add(SupplementType.OTHER);
        return list(Wrappers.<SupplementOrderPo>lambdaQuery().in(CollectionUtil.isNotEmpty(supplierCodeList), SupplementOrderPo::getSupplierCode, supplierCodeList)
                .le(examineTime != null, SupplementOrderPo::getExamineTime, examineTime)
                .ge(examineTimeStart != null, SupplementOrderPo::getExamineTime, examineTimeStart)
                .le(examineTimeEnd != null, SupplementOrderPo::getExamineTime, examineTimeEnd)
                .eq(SupplementOrderPo::getSupplementStatus, SupplementStatus.AUDITED)
                .in(SupplementOrderPo::getSupplementType, supplementTypeList)
                .eq(SupplementOrderPo::getSettleOrderNo, ""));
    }

    /**
     * 通过用户和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<SupplementOrderPo> getBySupplementUserAndExamineTime(String supplementUser, LocalDateTime examineTime,
                                                                     LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        List<SupplementType> supplementTypeList = new ArrayList<>();
        supplementTypeList.add(SupplementType.PROCESS);
        supplementTypeList.add(SupplementType.OTHER);
        return list(Wrappers.<SupplementOrderPo>lambdaQuery().eq(StringUtils.isNotBlank(supplementUser), SupplementOrderPo::getSupplementUser, supplementUser)
                .le(examineTime != null, SupplementOrderPo::getExamineTime, examineTime)
                .ge(examineTimeStart != null, SupplementOrderPo::getExamineTime, examineTimeStart)
                .le(examineTimeEnd != null, SupplementOrderPo::getExamineTime, examineTimeEnd)
                .eq(SupplementOrderPo::getSupplementStatus, SupplementStatus.AUDITED)
                .in(SupplementOrderPo::getSupplementType, supplementTypeList)
                .eq(SupplementOrderPo::getSettleOrderNo, ""));
    }

    /**
     * 通过编号查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public SupplementOrderPo getBySupplementOrderNo(String supplementOrderNo) {
        return baseMapper.selectOne(Wrappers.<SupplementOrderPo>lambdaQuery()
                .eq(SupplementOrderPo::getSupplementOrderNo, supplementOrderNo));
    }

    /**
     * 通过批量编号查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public List<SupplementOrderPo> getBySupplementOrderNoList(List<String> supplementOrderNoList) {
        if (CollectionUtil.isEmpty(supplementOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplementOrderPo>lambdaQuery()
                .in(SupplementOrderPo::getSupplementOrderNo, supplementOrderNoList));
    }

    /**
     * 通过编号查询和版本号查询
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public SupplementOrderPo getBySupplementOrderNo(String supplementOrderNo, Integer version) {
        return baseMapper.selectOne(Wrappers.<SupplementOrderPo>lambdaQuery()
                .eq(SupplementOrderPo::getSupplementOrderNo, supplementOrderNo)
                .eq(SupplementOrderPo::getVersion, version));
    }

    /**
     * 根据编号批量修改状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 16:20
     */
    public void updateBatchSupplementOrderNo(List<String> supplementOrderNos, SupplementStatus supplementStatus) {
        if (CollectionUtil.isNotEmpty(supplementOrderNos)) {
            final SupplementOrderPo supplementOrderPo = new SupplementOrderPo();
            supplementOrderPo.setSupplementStatus(supplementStatus);
            supplementOrderPo.setPayTime(LocalDateTime.now());
            baseMapper.update(supplementOrderPo, Wrappers.<SupplementOrderPo>lambdaUpdate()
                    .in(SupplementOrderPo::getSupplementOrderNo, supplementOrderNos));
        }
    }

    public List<SupplementOrderPurchaseExportBo> getPurchaseBatchSupplementOrderNo(List<String> supplementOrderNoList) {
        if (CollectionUtil.isEmpty(supplementOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getPurchaseBatchSupplementOrderNo(supplementOrderNoList);
    }

    /**
     * 通过补款员工和类型获取列表
     *
     * @author ChenWenLong
     * @date 2023/1/13 15:19
     */
    public List<SupplementOrderPo> getBySupplementUserAndType(@NotBlank String supplementUser, SupplementType supplementType) {
        return list(Wrappers.<SupplementOrderPo>lambdaQuery()
                .eq(SupplementOrderPo::getSupplementUser, supplementUser)
                .eq(supplementType != null, SupplementOrderPo::getSupplementType, supplementType));
    }

    /**
     * 通过审核时间和结算单查询数据列表
     *
     * @param examineTimeStart:
     * @param examineTimeEnd:
     * @param settleOrderNoList:
     * @param supplementTypeList:
     * @return List<DeductOrderPo>
     * @author ChenWenLong
     * @date 2023/6/7 15:43
     */
    public List<SupplementOrderPo> getByExamineTimeAndSettleOrderNo(LocalDateTime examineTimeStart,
                                                                    LocalDateTime examineTimeEnd,
                                                                    List<String> settleOrderNoList,
                                                                    List<SupplementType> supplementTypeList,
                                                                    List<String> supplierCodeList,
                                                                    List<String> supplementUserList) {
        return list(Wrappers.<SupplementOrderPo>lambdaQuery()
                .and(w -> w.and(w1 -> w1.ge(examineTimeStart != null, SupplementOrderPo::getExamineTime, examineTimeStart)
                                .le(examineTimeEnd != null, SupplementOrderPo::getExamineTime, examineTimeEnd))
                        .or(w2 -> w2.in(CollectionUtil.isNotEmpty(settleOrderNoList), SupplementOrderPo::getSettleOrderNo, settleOrderNoList)))
                .eq(SupplementOrderPo::getSupplementStatus, SupplementStatus.AUDITED)
                .in(CollectionUtil.isNotEmpty(supplementTypeList), SupplementOrderPo::getSupplementType, supplementTypeList)
                .in(CollectionUtil.isNotEmpty(supplierCodeList), SupplementOrderPo::getSupplierCode, supplierCodeList)
                .in(CollectionUtil.isNotEmpty(supplementUserList), SupplementOrderPo::getSupplementUser, supplementUserList)
        );
    }

    /**
     * 获取sku导出总数
     *
     * @param dto
     * @return
     */
    public Integer getExportSkuTotals(SupplementOrderDto dto) {
        return baseMapper.getExportSkuTotals(dto);
    }

    /**
     * 查询sku导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<SupplementOrderPo> getExportSkuList(Page<Void> page, SupplementOrderDto dto) {
        IPage<SupplementOrderPo> pageResult = baseMapper.getExportSkuList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

}
