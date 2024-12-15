package com.hete.supply.scm.server.supplier.purchase.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.supplier.dao.ShippingMarkItemDao;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkItemPo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/15 19:50
 */
@Service
@RequiredArgsConstructor
@Validated
public class ShippingMarkBaseService {
    private final ShippingMarkItemDao shippingMarkItemDao;

    public void checkDeliverNoHasShippingMark(String deliverOrderNo) {
        if (StringUtils.isBlank(deliverOrderNo)) {
            throw new BizException("异常的发货单号，请联系系统管理员!");
        }
        final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemDao.getListByDeliverOrderNo(deliverOrderNo);
        if (CollectionUtils.isNotEmpty(shippingMarkItemPoList)) {
            throw new ParamIllegalException("当前发货子单：{}关联了箱唛：{}，无法取消该发货单，请先作废箱唛后再操作！",
                    deliverOrderNo, shippingMarkItemPoList.get(0).getShippingMarkNo());
        }
    }
}
