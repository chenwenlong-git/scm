package com.hete.supply.scm.server.scm.qc.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 质检单收货单关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Component
@Validated
public class QcReceiveOrderDao extends BaseDao<QcReceiveOrderMapper, QcReceiveOrderPo> {

    public List<QcReceiveOrderPo> getQcReceiveListBySearchDto(QcSearchDto dto) {
        // 不存在查询条件的情况直接返回null
        if (StringUtils.isBlank(dto.getSupplierCode())
                && StringUtils.isBlank(dto.getDeliveryOrderNo())
                && StringUtils.isBlank(dto.getScmBizNo())
                && null == dto.getReceiveType()
                && StringUtils.isBlank(dto.getGoodsCategory())
                && StringUtils.isBlank(dto.getSmGoodsCategory())) {
            return null;
        }


        return baseMapper.selectList(Wrappers.<QcReceiveOrderPo>lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getSupplierCode()),
                        QcReceiveOrderPo::getSupplierCode, dto.getSupplierCode())
                .eq(StringUtils.isNotBlank(dto.getDeliveryOrderNo()),
                        QcReceiveOrderPo::getDeliveryOrderNo, dto.getDeliveryOrderNo())
                .eq(StringUtils.isNotBlank(dto.getScmBizNo()),
                        QcReceiveOrderPo::getScmBizNo, dto.getScmBizNo())
                .eq(null != dto.getReceiveType(), QcReceiveOrderPo::getReceiveType,
                        dto.getReceiveType())
                .eq(StringUtils.isNotBlank(dto.getGoodsCategory()),
                        QcReceiveOrderPo::getGoodsCategory, dto.getGoodsCategory())
                .eq(StringUtils.isNotBlank(dto.getSmGoodsCategory()),
                        QcReceiveOrderPo::getGoodsCategory, dto.getSmGoodsCategory()));

    }

    public List<QcReceiveOrderPo> getListByQcOrderNoList(List<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcReceiveOrderPo>lambdaQuery()
                .in(QcReceiveOrderPo::getQcOrderNo, qcOrderNoList));
    }

    public QcReceiveOrderPo getOneByReceiveNo(String receiveOrderNo) {
        if (StringUtils.isBlank(receiveOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<QcReceiveOrderPo>lambdaQuery()
                .eq(QcReceiveOrderPo::getReceiveOrderNo, receiveOrderNo)
                .last("limit 1"));
    }

    /**
     * 通过质检收货单号列表获取对应的质检收货订单列表。
     *
     * @param receiveOrderNos 质检收货单号列表
     * @return 包含质检收货订单信息的列表，如果未找到匹配的订单则返回空列表
     */
    public List<QcReceiveOrderPo> getByReceiveNos(List<String> receiveOrderNos) {
        // 如果质检收货单号列表为空，返回空列表
        if (CollectionUtils.isEmpty(receiveOrderNos)) {
            return Collections.emptyList();
        }

        // 使用Lambda查询，根据质检收货单号列表查询匹配的质检收货订单并返回结果列表
        return baseMapper.selectList(Wrappers.<QcReceiveOrderPo>lambdaQuery()
                .in(QcReceiveOrderPo::getReceiveOrderNo, receiveOrderNos));
    }


    public QcReceiveOrderPo getOneByQcOrderNo(String qcOrderNo) {
        if (StringUtils.isBlank(qcOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<QcReceiveOrderPo>lambdaQuery()
                .eq(QcReceiveOrderPo::getQcOrderNo, qcOrderNo));
    }

    public List<QcReceiveOrderPo> getListByReceiveNo(String receiveOrderNo) {
        if (StringUtils.isBlank(receiveOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcReceiveOrderPo>lambdaQuery()
                .eq(QcReceiveOrderPo::getReceiveOrderNo, receiveOrderNo)
                .orderByDesc(QcReceiveOrderPo::getCreateTime));
    }

    public List<QcReceiveOrderPo> getListByScmBizNoList(List<String> scmBizNoList) {
        if (CollectionUtils.isEmpty(scmBizNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcReceiveOrderPo>lambdaQuery().
                in(QcReceiveOrderPo::getScmBizNo, scmBizNoList));
    }
}
