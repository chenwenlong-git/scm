package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 海外仓条码子项表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Component
@Validated
public class OverseasWarehouseMsgItemDao extends BaseDao<OverseasWarehouseMsgItemMapper, OverseasWarehouseMsgItemPo> {

    public List<OverseasWarehouseMsgItemPo> getListByOverseasMsgId(Long overseasWarehouseMsgId) {
        if (null == overseasWarehouseMsgId) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<OverseasWarehouseMsgItemPo>lambdaQuery()
                .eq(OverseasWarehouseMsgItemPo::getOverseasWarehouseMsgId, overseasWarehouseMsgId));
    }

    public void deleteByMsgId(Long overseasWarehouseMsgId) {
        baseMapper.deleteSkipCheck(Wrappers.<OverseasWarehouseMsgItemPo>lambdaUpdate()
                .eq(OverseasWarehouseMsgItemPo::getOverseasWarehouseMsgId, overseasWarehouseMsgId));
    }
}
