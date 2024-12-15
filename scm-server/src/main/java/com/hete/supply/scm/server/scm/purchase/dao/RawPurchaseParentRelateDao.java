package com.hete.supply.scm.server.scm.purchase.dao;

import com.hete.supply.scm.server.scm.purchase.entity.po.RawPurchaseParentRelatePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 采购子单与原料采购母单关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-21
 */
@Component
@Validated
public class RawPurchaseParentRelateDao extends BaseDao<RawPurchaseParentRelateMapper, RawPurchaseParentRelatePo> {

}
