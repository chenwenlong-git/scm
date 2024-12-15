package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessTemplateQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplatePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessTemplateType;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工序模版表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessTemplateMapper extends BaseDataMapper<ProcessTemplatePo> {


    /**
     * 通过分页查询
     *
     * @param page
     * @param processTemplateQueryDto
     * @param categoryType
     * @param skuType
     * @return
     */
    IPage<ProcessTemplateVo> getByPage(Page<Void> page,
                                       @Param("params") ProcessTemplateQueryDto processTemplateQueryDto,
                                       @Param("categoryType") ProcessTemplateType categoryType,
                                       @Param("skuType") ProcessTemplateType skuType);
}
