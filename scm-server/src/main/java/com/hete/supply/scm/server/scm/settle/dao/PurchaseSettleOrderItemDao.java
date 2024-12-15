package com.hete.supply.scm.server.scm.settle.dao;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderProductDto;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购结算单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Component
@Validated
public class PurchaseSettleOrderItemDao extends BaseDao<PurchaseSettleOrderItemMapper, PurchaseSettleOrderItemPo> {

    /**
     * 根据条件查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 14:49
     */
    public List<PurchaseSettleOrderItemPo> searchPurchaseSettleOrderItem(PurchaseSettleOrderProductDto dto) {
        return list(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery().eq(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId, dto.getPurchaseSettleOrderId())
                .eq(dto.getPurchaseSettleItemType() != null, PurchaseSettleOrderItemPo::getPurchaseSettleItemType, dto.getPurchaseSettleItemType())
                .like(StringUtils.isNotBlank(dto.getBusinessNo()), PurchaseSettleOrderItemPo::getBusinessNo, dto.getBusinessNo())
                .orderByDesc(PurchaseSettleOrderItemPo::getSettleTime));
    }

    /**
     * 通过ID和版本查询信息
     *
     * @author ChenWenLong
     * @date 2022/11/9 17:12
     */
    public List<PurchaseSettleOrderItemPo> getByBatchIds(List<Long> purchaseSettleOrderItemIds) {
        return baseMapper.selectBatchIds(purchaseSettleOrderItemIds);
    }

    /**
     * 通过ID批量删除
     *
     * @author ChenWenLong
     * @date 2022/11/14 15:04
     */
    public int removeBatchByIds(List<Long> purchaseSettleOrderItemIds) {
        return baseMapper.deleteBatchIds(purchaseSettleOrderItemIds);
    }

    /**
     * 根据编号查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 14:49
     */
    public List<PurchaseSettleOrderItemPo> getByBusinessNo(String businessNo) {
        return list(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery()
                .like(PurchaseSettleOrderItemPo::getBusinessNo, businessNo));
    }

    /**
     * 根据编号查询单条最新记录
     *
     * @author ChenWenLong
     * @date 2023/1/18 09:47
     */
    public PurchaseSettleOrderItemPo getItemByBusinessNo(String businessNo) {
        return baseMapper.selectOne(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery()
                .eq(PurchaseSettleOrderItemPo::getBusinessNo, businessNo)
                .orderByDesc(PurchaseSettleOrderItemPo::getCreateTime)
                .last("limit 1"));
    }


    public List<PurchaseSettleOrderItemPo> getByBusinessNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery()
                .in(PurchaseSettleOrderItemPo::getBusinessNo, childOrderNoList)
                .orderByDesc(PurchaseSettleOrderItemPo::getCreateTime));
    }

    /**
     * 根据采购结算单号查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 14:49
     */
    public List<PurchaseSettleOrderItemPo> getByPurchaseSettleOrderNo(String purchaseSettleOrderNo, PurchaseSettleItemType purchaseSettleItemType) {
        return list(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery()
                .like(PurchaseSettleOrderItemPo::getPurchaseSettleOrderNo, purchaseSettleOrderNo)
                .eq(purchaseSettleItemType != null, PurchaseSettleOrderItemPo::getPurchaseSettleItemType, purchaseSettleItemType));
    }

    /**
     * 根据结算单号批量查询返回map集合
     *
     * @param purchaseSettleOrderNoList:
     * @return Map<String, List < PurchaseSettleOrderItemPo>>
     * @author ChenWenLong
     * @date 2023/6/6 11:28
     */
    public Map<String, List<PurchaseSettleOrderItemPo>> getMapByBatchPurchaseSettleOrderNo(List<String> purchaseSettleOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseSettleOrderNoList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery()
                .in(PurchaseSettleOrderItemPo::getPurchaseSettleOrderNo, purchaseSettleOrderNoList)).stream()
                .collect(Collectors.groupingBy(PurchaseSettleOrderItemPo::getPurchaseSettleOrderNo));
    }


    /**
     * 根据结算单号批量查询返回list集合
     *
     * @param purchaseSettleOrderNoList:
     * @return List<PurchaseSettleOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/1 11:40
     */
    public List<PurchaseSettleOrderItemPo> getListByBatchPurchaseSettleOrderNo(List<String> purchaseSettleOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseSettleOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<PurchaseSettleOrderItemPo>lambdaQuery()
                .in(PurchaseSettleOrderItemPo::getPurchaseSettleOrderNo, purchaseSettleOrderNoList));
    }

}
