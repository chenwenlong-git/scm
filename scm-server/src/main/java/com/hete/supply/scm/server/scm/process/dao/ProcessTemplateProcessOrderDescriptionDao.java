package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplateProcessOrderDescriptionPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 工序模板-加工单工序描述 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-09-09
 */
@Component
@Validated
public class ProcessTemplateProcessOrderDescriptionDao extends BaseDao<ProcessTemplateProcessOrderDescriptionMapper, ProcessTemplateProcessOrderDescriptionPo> {

    public List<ProcessTemplateProcessOrderDescriptionPo> getByProcessTemplateId(Long processTemplateId) {
        return list(Wrappers.<ProcessTemplateProcessOrderDescriptionPo>lambdaQuery()
                .eq(ProcessTemplateProcessOrderDescriptionPo::getProcessTemplateId, processTemplateId));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
