package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.supplier.entity.dto.ShippingMarkListDto;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkPo;
import com.hete.supply.scm.server.supplier.enums.ShippingMarkStatus;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 箱唛表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Component
@Validated
public class ShippingMarkDao extends BaseDao<ShippingMarkMapper, ShippingMarkPo> {

    public IPage<ShippingMarkPo> shippingMarkList(Page<ShippingMarkPo> page, ShippingMarkListDto dto, List<String> shippingMarkNoList) {
        return baseMapper.selectPage(page, Wrappers.<ShippingMarkPo>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(dto.getAuthSupplierCode()), ShippingMarkPo::getSupplierCode, dto.getAuthSupplierCode())
                .like(StringUtils.isNotBlank(dto.getShippingMarkNo()), ShippingMarkPo::getShippingMarkNo, dto.getShippingMarkNo())
                .like(StringUtils.isNotBlank(dto.getDeliverUser()), ShippingMarkPo::getDeliverUser, dto.getDeliverUser())
                .like(StringUtils.isNotBlank(dto.getDeliverUsername()), ShippingMarkPo::getDeliverUsername, dto.getDeliverUsername())
                .ge(null != dto.getDeliverTimeStart(), ShippingMarkPo::getDeliverTime, dto.getDeliverTimeStart())
                .le(null != dto.getDeliverTimeEnd(), ShippingMarkPo::getDeliverTime, dto.getDeliverTimeEnd())
                .in(CollectionUtils.isNotEmpty(dto.getWarehouseCodeList()), ShippingMarkPo::getWarehouseCode, dto.getWarehouseCodeList())
                .in(CollectionUtils.isNotEmpty(dto.getWarehouseNameList()), ShippingMarkPo::getWarehouseName, dto.getWarehouseNameList())
                .in(CollectionUtils.isNotEmpty(dto.getSupplierCodeList()), ShippingMarkPo::getSupplierCode, dto.getSupplierCodeList())
                .in(CollectionUtils.isNotEmpty(dto.getSupplierNameList()), ShippingMarkPo::getSupplierName, dto.getSupplierNameList())
                .in(CollectionUtils.isNotEmpty(dto.getShippingMarkStatusList()), ShippingMarkPo::getShippingMarkStatus, dto.getShippingMarkStatusList())
                .in(CollectionUtils.isNotEmpty(shippingMarkNoList), ShippingMarkPo::getShippingMarkNo, shippingMarkNoList)
                .like(StringUtils.isNotBlank(dto.getWarehouseName()), ShippingMarkPo::getWarehouseName, dto.getWarehouseName())
                .orderByDesc(ShippingMarkPo::getCreateTime));
    }

    public ShippingMarkPo getOneByNo(String shippingMarkNo) {
        if (StringUtils.isBlank(shippingMarkNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<ShippingMarkPo>lambdaQuery()
                .eq(ShippingMarkPo::getShippingMarkNo, shippingMarkNo));
    }

    public ShippingMarkPo getOneByTrackingNo(String trackingNo) {
        if (StringUtils.isBlank(trackingNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<ShippingMarkPo>lambdaQuery()
                .eq(ShippingMarkPo::getTrackingNo, trackingNo));
    }


    public Map<String, ShippingMarkPo> getShippingMarkNoMapByNoList(List<String> shippingMarkNoList) {
        if (CollectionUtils.isEmpty(shippingMarkNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<ShippingMarkPo>lambdaQuery()
                        .in(ShippingMarkPo::getShippingMarkNo, shippingMarkNoList))
                .stream()
                .collect(Collectors.toMap(ShippingMarkPo::getShippingMarkNo, Function.identity()));
    }

    public List<ShippingMarkPo> getListByNoList(List<String> shippingMarkNoList) {
        if (CollectionUtils.isEmpty(shippingMarkNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ShippingMarkPo>lambdaQuery()
                .in(ShippingMarkPo::getShippingMarkNo, shippingMarkNoList));
    }

    public ShippingMarkPo getOneByNoAndStatus(String shippingMarkNo, List<ShippingMarkStatus> shippingMarkStatusList) {
        if (StringUtils.isBlank(shippingMarkNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<ShippingMarkPo>lambdaQuery()
                .eq(ShippingMarkPo::getShippingMarkNo, shippingMarkNo)
                .in(ShippingMarkPo::getShippingMarkStatus, shippingMarkStatusList));
    }
}
