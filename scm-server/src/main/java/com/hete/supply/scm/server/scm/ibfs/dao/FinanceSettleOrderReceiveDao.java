package com.hete.supply.scm.server.scm.ibfs.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderReceivePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务结算单收款表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-14
 */
@Component
@Validated
public class FinanceSettleOrderReceiveDao extends BaseDao<FinanceSettleOrderReceiveMapper, FinanceSettleOrderReceivePo> {

    public List<FinanceSettleOrderReceivePo> findByFinanceSettleOrderNo(String financeSettleOrderNo) {
        if (StrUtil.isBlank(financeSettleOrderNo)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<FinanceSettleOrderReceivePo>lambdaQuery()
                .eq(FinanceSettleOrderReceivePo::getFinanceSettleOrderNo, financeSettleOrderNo));
    }
}
