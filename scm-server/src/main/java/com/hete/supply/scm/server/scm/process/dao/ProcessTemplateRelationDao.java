package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplateRelationPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 工序模版关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessTemplateRelationDao extends BaseDao<ProcessTemplateRelationMapper, ProcessTemplateRelationPo> {

    /**
     * 通过工序模版查询
     *
     * @param processTemplateId
     * @return
     */
    public List<ProcessTemplateRelationPo> getByProcessTemplateId(Long processTemplateId) {
        return list(Wrappers.<ProcessTemplateRelationPo>lambdaQuery()
                .eq(ProcessTemplateRelationPo::getProcessTemplateId, processTemplateId));
    }

    /**
     * 通过多个工序模版 id 查询
     *
     * @param processTemplateIds
     * @return
     */
    public List<ProcessTemplateRelationPo> getByProcessTemplateIds(List<Long> processTemplateIds) {
        return list(Wrappers.<ProcessTemplateRelationPo>lambdaQuery()
                .in(ProcessTemplateRelationPo::getProcessTemplateId, processTemplateIds));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    @Override
    public boolean updateBatchById(Collection<ProcessTemplateRelationPo> entityList) {
        return super.updateBatchById(entityList);
    }
}
