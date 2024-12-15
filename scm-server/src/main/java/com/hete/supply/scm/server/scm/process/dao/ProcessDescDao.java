package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessDescPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 加工描述表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessDescDao extends BaseDao<ProcessDescMapper, ProcessDescPo> {

    /**
     * 查询分页
     *
     * @param page
     * @param processDescQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessDescVo> getByPage(Page<Void> page, ProcessDescQueryDto processDescQueryDto) {
        IPage<ProcessDescVo> pageResult = baseMapper.getByPage(page, processDescQueryDto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过名称查询加工描述
     *
     * @param name
     * @return
     */
    public ProcessDescPo getByName(String name, ProcessFirst processFirst) {
        return getOne(Wrappers.<ProcessDescPo>lambdaQuery()
                .eq(ProcessDescPo::getName, name)
                .eq(ProcessDescPo::getProcessFirst, processFirst)
        );

    }
}
