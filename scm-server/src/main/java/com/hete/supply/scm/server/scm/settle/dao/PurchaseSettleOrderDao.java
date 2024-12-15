package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购结算单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class PurchaseSettleOrderDao extends BaseDao<PurchaseSettleOrderMapper, PurchaseSettleOrderPo> {

    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 10:34
     */
    public CommonPageResult.PageInfo<PurchaseSettleOrderVo> searchPurchaseSettleOrder(Page<Void> page, PurchaseSettleOrderSearchDto purchaseSettleOrderSearchDto) {
        IPage<PurchaseSettleOrderVo> pageResult = baseMapper.selectPurchaseSettleOrderPage(page, purchaseSettleOrderSearchDto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过供应商和月份查询
     *
     * @author ChenWenLong
     * @date 2022/11/23 09:59
     */
    public PurchaseSettleOrderPo getBySupplierCodeAndMonth(String supplierCode, String month) {
        return baseMapper.selectOne(Wrappers.<PurchaseSettleOrderPo>lambdaQuery()
                .eq(PurchaseSettleOrderPo::getSupplierCode, supplierCode)
                .eq(PurchaseSettleOrderPo::getMonth, month));
    }

    public Map<String, PurchaseSettleOrderPo> getMapByNoList(List<String> settleOrderNoList) {
        if (CollectionUtils.isEmpty(settleOrderNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<PurchaseSettleOrderPo>lambdaQuery()
                        .in(PurchaseSettleOrderPo::getPurchaseSettleOrderNo, settleOrderNoList))
                .stream()
                .collect(Collectors.toMap(PurchaseSettleOrderPo::getPurchaseSettleOrderNo, Function.identity()));
    }

    /**
     * 统计导出的总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(PurchaseSettleOrderSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<PurchaseSettleOrderExportVo> getExportList(Page<Void> page, PurchaseSettleOrderSearchDto dto) {
        IPage<PurchaseSettleOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 根据供应商代码查询待确认状态
     *
     * @author ChenWenLong
     * @date 2023/1/31 10:47
     */
    public List<PurchaseSettleOrderPo> getListBySupplierCode(String supplierCode, String purchaseSettleOrderNo, PurchaseSettleStatus purchaseSettleStatus) {
        return list(Wrappers.<PurchaseSettleOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(supplierCode), PurchaseSettleOrderPo::getSupplierCode, supplierCode)
                .eq(purchaseSettleStatus != null, PurchaseSettleOrderPo::getPurchaseSettleStatus, purchaseSettleStatus)
                .like(StringUtils.isNotBlank(purchaseSettleOrderNo), PurchaseSettleOrderPo::getPurchaseSettleOrderNo, purchaseSettleOrderNo));
    }

    /**
     * 通过编号查询单记录
     *
     * @author ChenWenLong
     * @date 2023/1/31 14:02
     */
    public PurchaseSettleOrderPo getByPurchaseSettleOrderNo(String purchaseSettleOrderNo) {
        return baseMapper.selectOne(Wrappers.<PurchaseSettleOrderPo>lambdaQuery()
                .eq(PurchaseSettleOrderPo::getPurchaseSettleOrderNo, purchaseSettleOrderNo));
    }

    /**
     * 根据供应商和月份查询
     *
     * @author ChenWenLong
     * @date 2023/2/4 18:12
     */
    public List<PurchaseSettleOrderPo> getListBySupplierCodeAndMonth(String supplierCode, String month) {
        return list(Wrappers.<PurchaseSettleOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(supplierCode), PurchaseSettleOrderPo::getSupplierCode, supplierCode)
                .eq(StringUtils.isNotBlank(month), PurchaseSettleOrderPo::getMonth, month));
    }

    /**
     * 通过支付时间查询
     *
     * @param payTimeStart:
     * @param payTimeEnd:
     * @return List<PurchaseSettleOrderPo>
     * @author ChenWenLong
     * @date 2023/7/1 11:37
     */
    public List<PurchaseSettleOrderPo> getListByPayTime(@NotNull LocalDateTime payTimeStart, @NotNull LocalDateTime payTimeEnd) {
        return list(Wrappers.<PurchaseSettleOrderPo>lambdaQuery()
                .ge(PurchaseSettleOrderPo::getPayTime, payTimeStart)
                .le(PurchaseSettleOrderPo::getPayTime, payTimeEnd));
    }
}
