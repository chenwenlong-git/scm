package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePrepaymentOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 预付款单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-10
 */
@Component
@Validated
public class FinancePrepaymentOrderItemDao extends BaseDao<FinancePrepaymentOrderItemMapper, FinancePrepaymentOrderItemPo> {

    public List<FinancePrepaymentOrderItemPo> getListByNo(String prepaymentOrderNo) {
        if (StringUtils.isBlank(prepaymentOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinancePrepaymentOrderItemPo>lambdaQuery()
                .eq(FinancePrepaymentOrderItemPo::getPrepaymentOrderNo, prepaymentOrderNo));
    }
}
