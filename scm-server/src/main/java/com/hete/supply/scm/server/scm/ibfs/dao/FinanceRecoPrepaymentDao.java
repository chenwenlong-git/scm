package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoPrepaymentPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 对账-预付款关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-10
 */
@Component
@Validated
public class FinanceRecoPrepaymentDao extends BaseDao<FinanceRecoPrepaymentMapper, FinanceRecoPrepaymentPo> {

    public List<FinanceRecoPrepaymentPo> getListByPrepaymentNoList(List<String> prepaymentOrderNoList) {
        if (CollectionUtils.isEmpty(prepaymentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<FinanceRecoPrepaymentPo>lambdaQuery()
                .in(FinanceRecoPrepaymentPo::getPrepaymentOrderNo, prepaymentOrderNoList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public FinanceRecoPrepaymentPo getOneByPrepaymentNo(String prepaymentOrderNo) {
        if (StringUtils.isBlank(prepaymentOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<FinanceRecoPrepaymentPo>lambdaQuery()
                .eq(FinanceRecoPrepaymentPo::getPrepaymentOrderNo, prepaymentOrderNo));
    }
}
