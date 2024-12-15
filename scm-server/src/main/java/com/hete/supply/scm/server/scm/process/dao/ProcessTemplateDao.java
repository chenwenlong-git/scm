package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessTemplateQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplatePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessTemplateType;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 工序模版表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessTemplateDao extends BaseDao<ProcessTemplateMapper, ProcessTemplatePo> {


    /**
     * 分页查询
     *
     * @param page
     * @param processTemplateQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessTemplateVo> getByPage(Page<Void> page, ProcessTemplateQueryDto processTemplateQueryDto) {
        IPage<ProcessTemplateVo> pageResult = baseMapper.getByPage(page, processTemplateQueryDto, ProcessTemplateType.CATEGORY, ProcessTemplateType.SKU);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过名称查询
     *
     * @param name
     * @return
     */
    public ProcessTemplatePo getByName(String name) {
        return getOne(Wrappers.<ProcessTemplatePo>lambdaQuery()
                .eq(ProcessTemplatePo::getName, name));
    }

    /**
     * 通过名称模糊查询
     *
     * @param name
     * @return
     */
    public List<ProcessTemplatePo> getListByName(String name) {
        return list(Wrappers.<ProcessTemplatePo>lambdaQuery()
                .like(ProcessTemplatePo::getName, name));
    }

    /**
     * 通过 sku 模糊查询
     *
     * @param sku
     * @return
     */
    public List<ProcessTemplatePo> getListBySku(String sku) {
        return list(Wrappers.<ProcessTemplatePo>lambdaQuery()
                .eq(ProcessTemplatePo::getProcessTemplateType, ProcessTemplateType.SKU)
                .like(ProcessTemplatePo::getTypeValueName, sku));
    }


    /**
     * 通过品类查询
     *
     * @param categoryIds
     * @return
     */
    public List<ProcessTemplatePo> getListByCategoryIds(List<Long> categoryIds) {
        return list(Wrappers.<ProcessTemplatePo>lambdaQuery()
                .eq(ProcessTemplatePo::getProcessTemplateType, ProcessTemplateType.CATEGORY)
                .in(ProcessTemplatePo::getTypeValue, categoryIds));
    }

    public ProcessTemplatePo getNewestProcessTemplate(String sku, ProcessTemplateType processTemplateType) {
        List<ProcessTemplatePo> processTemplatePos = list(Wrappers.<ProcessTemplatePo>lambdaQuery()
                .eq(ProcessTemplatePo::getProcessTemplateType, processTemplateType)
                .eq(ProcessTemplatePo::getTypeValue, sku).
                orderByDesc(ProcessTemplatePo::getCreateTime));
        if (CollectionUtils.isEmpty(processTemplatePos)) {
            return null;
        }
        return processTemplatePos.stream().findFirst().orElse(null);
    }
}
