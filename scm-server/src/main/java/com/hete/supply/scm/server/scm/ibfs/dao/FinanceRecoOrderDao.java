package com.hete.supply.scm.server.scm.ibfs.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportItemVo;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportVo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderBatchUpdateInfoBo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务对账单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceRecoOrderDao extends BaseDao<FinanceRecoOrderMapper, FinanceRecoOrderPo> {

    public CommonPageResult.PageInfo<RecoOrderSearchVo> searchRecoOrderPage(Page<Void> page, RecoOrderSearchDto dto) {
        IPage<RecoOrderSearchVo> pageResult = baseMapper.searchRecoOrderPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getExportTotals(RecoOrderSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<RecoOrderExportVo> getExportList(Page<Void> page, RecoOrderSearchDto dto) {
        IPage<RecoOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getExportTotalsItem(RecoOrderSearchDto dto) {
        return baseMapper.getExportTotalsItem(dto);
    }

    public CommonPageResult.PageInfo<RecoOrderExportItemVo> getExportListItem(Page<Void> page, RecoOrderSearchDto dto) {
        IPage<RecoOrderExportItemVo> pageResult = baseMapper.getExportListItem(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }


    public List<String> getSupplierList(RecoOrderSearchDto dto) {
        return baseMapper.getSupplierList(dto);
    }

    public List<FinanceRecoOrderPo> getListByNoList(List<String> financeRecoOrderNoList) {

        if (CollectionUtils.isEmpty(financeRecoOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .in(FinanceRecoOrderPo::getFinanceRecoOrderNo, financeRecoOrderNoList));

    }

    public FinanceRecoOrderPo getOneByNo(String financeRecoOrderNo) {
        if (StringUtils.isBlank(financeRecoOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .eq(FinanceRecoOrderPo::getFinanceRecoOrderNo, financeRecoOrderNo));
    }

    public FinanceRecoOrderPo getOneByNoAndVersion(String financeRecoOrderNo, Integer version) {
        if (StringUtils.isBlank(financeRecoOrderNo) || null == version) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .eq(FinanceRecoOrderPo::getFinanceRecoOrderNo, financeRecoOrderNo)
                .eq(FinanceRecoOrderPo::getVersion, version));

    }

    /**
     * 根据供应商编码和对账状态查询财务对账单列表。
     *
     * @param supplierCode 供应商编码
     * @param recoStatus   对账状态
     * @return 符合条件的财务对账单列表
     */
    public List<FinanceRecoOrderPo> getRecoOrdersBySupplierCodeAndStatus(String supplierCode,
                                                                         FinanceRecoOrderStatus recoStatus) {
        // 创建 LambdaQueryWrapper 构建查询条件
        LambdaQueryWrapper<FinanceRecoOrderPo> queryWrapper = new LambdaQueryWrapper<>();

        // 添加供应商编码和对账状态的查询条件
        queryWrapper.eq(StrUtil.isNotBlank(supplierCode), FinanceRecoOrderPo::getSupplierCode, supplierCode)
                .eq(FinanceRecoOrderPo::getFinanceRecoOrderStatus, recoStatus);

        // 执行查询并返回结果
        return baseMapper.selectList(queryWrapper);
    }

    public List<FinanceRecoOrderPo> getListBySupplierCodeListAndStatus(List<String> supplierCodeList,
                                                                       FinanceRecoOrderStatus recoStatus) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .in(FinanceRecoOrderPo::getSupplierCode, supplierCodeList)
                .eq(null != recoStatus, FinanceRecoOrderPo::getFinanceRecoOrderStatus, recoStatus));
    }

    public List<FinanceRecoOrderPo> getListBySupplierCodeAndNotStatus(String supplierCode,
                                                                      FinanceRecoOrderStatus recoStatus,
                                                                      LocalDateTime reconciliationTime) {
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .eq(FinanceRecoOrderPo::getSupplierCode, supplierCode)
                .ne(null != recoStatus, FinanceRecoOrderPo::getFinanceRecoOrderStatus, recoStatus)
                .le(null != reconciliationTime, FinanceRecoOrderPo::getReconciliationStartTime, reconciliationTime)
                .ge(null != reconciliationTime, FinanceRecoOrderPo::getReconciliationEndTime, reconciliationTime));
    }

    public List<FinanceRecoOrderPo> getListByStatus(FinanceRecoOrderStatus recoStatus) {
        if (null == recoStatus) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .eq(FinanceRecoOrderPo::getFinanceRecoOrderStatus, recoStatus));
    }


    public void updateInfoByBatchId(List<Long> idList, RecoOrderBatchUpdateInfoBo bo) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        baseMapper.updateInfoByBatchId(idList, bo);
    }

    public List<FinanceRecoOrderPo> getListByPriceAndStatus(@NotNull FinanceRecoOrderStatus recoStatus,
                                                            BigDecimal receivePrice,
                                                            BigDecimal payPrice,
                                                            List<String> supplierCodeList) {
        if (null == recoStatus) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderPo>lambdaQuery()
                .eq(FinanceRecoOrderPo::getFinanceRecoOrderStatus, recoStatus)
                .eq(null != receivePrice, FinanceRecoOrderPo::getReceivePrice, receivePrice)
                .eq(null != payPrice, FinanceRecoOrderPo::getPayPrice, payPrice)
                .in(CollectionUtils.isNotEmpty(supplierCodeList), FinanceRecoOrderPo::getSupplierCode, supplierCodeList));
    }

}
