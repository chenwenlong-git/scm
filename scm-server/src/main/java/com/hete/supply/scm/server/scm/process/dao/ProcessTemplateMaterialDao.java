package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplateMaterialPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 工序模版原料表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-04-03
 */
@Component
@Validated
public class ProcessTemplateMaterialDao extends BaseDao<ProcessTemplateMaterialMapper, ProcessTemplateMaterialPo> {

    /**
     * 通过工序模版 id 查询原料列表
     *
     * @param processTemplateId
     * @return
     */
    public List<ProcessTemplateMaterialPo> getByProcessTemplateId(Long processTemplateId) {
        return list(Wrappers.<ProcessTemplateMaterialPo>lambdaQuery()
                .eq(ProcessTemplateMaterialPo::getProcessTemplateId, processTemplateId));
    }

    /**
     * 通过多个工序模版 id 查询原料列表
     *
     * @param processTemplateIds
     * @return
     */
    public List<ProcessTemplateMaterialPo> getByProcessTemplateIds(List<Long> processTemplateIds) {
        return list(Wrappers.<ProcessTemplateMaterialPo>lambdaQuery()
                .in(ProcessTemplateMaterialPo::getProcessTemplateId, processTemplateIds));
    }

    /**
     * 根据一组原料SKU获取相关的工艺模板物料信息列表。
     *
     * @param materialSkus 物料SKU列表
     * @return 包含匹配的工艺模板原料信息的列表
     */
    public List<ProcessTemplateMaterialPo> getBySkus(List<String> materialSkus) {
        return list(Wrappers.<ProcessTemplateMaterialPo>lambdaQuery().in(ProcessTemplateMaterialPo::getSku, materialSkus));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

}
