package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCommissionRulePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 工序提成规则 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-14
 */
@Component
@Validated
public class ProcessCommissionRuleDao extends BaseDao<ProcessCommissionRuleMapper, ProcessCommissionRulePo> {

    public List<ProcessCommissionRulePo> listByProcessCodes(Collection<String> processCodes) {
        if (CollectionUtils.isEmpty(processCodes)) {
            return Collections.emptyList();
        }

        // 使用MyBatis Plus的lambdaQuery进行查询，根据工序ID集合进行in条件查询
        return list(Wrappers.<ProcessCommissionRulePo>lambdaQuery()
                .in(ProcessCommissionRulePo::getProcessCode, processCodes)
                .orderByAsc(ProcessCommissionRulePo::getCommissionLevel));
    }

    public List<ProcessCommissionRulePo> listByProcessCode(String processCode) {
        if (StringUtils.isBlank(processCode)) {
            return Collections.emptyList();
        }

        // 使用MyBatis Plus的lambdaQuery进行查询，根据工序ID集合进行in条件查询
        return list(Wrappers.<ProcessCommissionRulePo>lambdaQuery()
                .eq(ProcessCommissionRulePo::getProcessCode, processCode)
                .orderByAsc(ProcessCommissionRulePo::getCommissionLevel));
    }

    public boolean removeBatchByIds(Collection<?> ids) {
        return super.removeBatchByIds(ids);
    }
}
