package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 供应商产品 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-25
 */
@Component
@Validated
public class SupplierProductDao extends BaseDao<SupplierProductMapper, SupplierProductPo> {

    /**
     * 通过供应商代码获取对应的列表
     *
     * @author ChenWenLong
     * @date 2022/11/26 16:58
     */
    public List<SupplierProductPo> getBySupplierCode(String supplierCode) {
        return list(Wrappers.<SupplierProductPo>lambdaQuery().eq(SupplierProductPo::getSupplierCode, supplierCode));
    }

    /**
     * 批量删除
     *
     * @author ChenWenLong
     * @date 2022/11/26 18:22
     */
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

}
