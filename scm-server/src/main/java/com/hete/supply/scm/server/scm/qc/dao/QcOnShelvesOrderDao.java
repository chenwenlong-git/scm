package com.hete.supply.scm.server.scm.qc.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOnShelvesOrderPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 质检单上架单关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Component
@Validated
public class QcOnShelvesOrderDao extends BaseDao<QcOnShelvesOrderMapper, QcOnShelvesOrderPo> {

    public List<QcOnShelvesOrderPo> getListByQcOrderNoList(List<String> qcOrderNoList) {
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcOnShelvesOrderPo>lambdaQuery()
                .in(QcOnShelvesOrderPo::getQcOrderNo, qcOrderNoList));
    }

    public List<QcOnShelvesOrderPo> getListByQcOrderNo(String qcOrderNo) {
        if (StringUtils.isBlank(qcOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<QcOnShelvesOrderPo>lambdaQuery()
                .eq(QcOnShelvesOrderPo::getQcOrderNo, qcOrderNo));
    }

    public QcOnShelvesOrderPo getOneByQcOrderNoExcludeType(String qcOrderNo, WmsEnum.OnShelvesOrderCreateType type) {
        if (StringUtils.isBlank(qcOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<QcOnShelvesOrderPo>lambdaQuery()
                .eq(QcOnShelvesOrderPo::getQcOrderNo, qcOrderNo)
                .ne(QcOnShelvesOrderPo::getType, type));
    }

    /**
     * 通过上架单号列表获取上架信息
     *
     * @param onShelvesOrderNos 上架单号列表
     * @return 匹配上架单号的上架信息列表
     */
    public List<QcOnShelvesOrderPo> getOnShelvesOrdersByOnShelvesOrderNos(Collection<String> onShelvesOrderNos) {
        if (CollectionUtils.isEmpty(onShelvesOrderNos)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wrappers.<QcOnShelvesOrderPo>lambdaQuery()
                        .in(QcOnShelvesOrderPo::getOnShelvesOrderNo, onShelvesOrderNos)
        );
    }

    /**
     * 通过上架单号列表获取上架信息
     *
     * @param onShelvesOrderNo 上架单号列表
     * @return 匹配上架单号的上架信息列表
     */
    public QcOnShelvesOrderPo getOnShelvesOrdersByOnShelvesOrderNo(String onShelvesOrderNo) {
        if (StrUtil.isBlank(onShelvesOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<QcOnShelvesOrderPo>lambdaQuery().eq(QcOnShelvesOrderPo::getOnShelvesOrderNo, onShelvesOrderNo));
    }


}
