package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCompositePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 复合工序关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-26
 */
@Component
@Validated
public class ProcessCompositeDao extends BaseDao<ProcessCompositeMapper, ProcessCompositePo> {

    public List<ProcessCompositePo> listByParentProcessCodes(Set<String> processCodes) {
        return CollectionUtils.isEmpty(processCodes) ?
                Collections.emptyList() :
                list(Wrappers.<ProcessCompositePo>lambdaQuery()
                        .in(ProcessCompositePo::getParentProcessCode, processCodes));
    }

    public List<ProcessCompositePo> listBySubProcessCodes(Set<String> processCodes) {
        return CollectionUtils.isEmpty(processCodes) ?
                Collections.emptyList() :
                list(Wrappers.<ProcessCompositePo>lambdaQuery()
                        .in(ProcessCompositePo::getSubProcessCode, processCodes));
    }

    public List<ProcessCompositePo> list() {
        return list(Wrappers.<ProcessCompositePo>lambdaQuery());
    }
}
