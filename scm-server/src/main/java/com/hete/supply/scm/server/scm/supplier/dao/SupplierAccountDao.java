package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierAccountPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 供应商账号信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Component
@Validated
public class SupplierAccountDao extends BaseDao<SupplierAccountMapper, SupplierAccountPo> {

    /**
     * 通过供应商代码获取对应的列表
     *
     * @author ChenWenLong
     * @date 2022/11/26 16:58
     */
    public List<SupplierAccountPo> getBySupplierCode(String supplierCode) {
        return list(Wrappers.<SupplierAccountPo>lambdaQuery().eq(SupplierAccountPo::getSupplierCode, supplierCode));
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
