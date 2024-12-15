package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 海外仓信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Component
@Validated
public class OverseasWarehouseMsgDao extends BaseDao<OverseasWarehouseMsgMapper, OverseasWarehouseMsgPo> {

    public OverseasWarehouseMsgPo getByPurchaseChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<OverseasWarehouseMsgPo>lambdaQuery()
                .eq(OverseasWarehouseMsgPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public OverseasWarehouseMsgPo getByNo(String overseasShippingMarkNo) {
        if (StringUtils.isBlank(overseasShippingMarkNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<OverseasWarehouseMsgPo>lambdaQuery()
                .eq(OverseasWarehouseMsgPo::getOverseasShippingMarkNo, overseasShippingMarkNo));
    }
}
