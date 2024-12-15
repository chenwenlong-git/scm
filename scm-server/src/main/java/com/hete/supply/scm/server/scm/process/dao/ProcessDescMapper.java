package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessDescPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 加工描述表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessDescMapper extends BaseDataMapper<ProcessDescPo> {

    /**
     * 查询分页
     *
     * @param page
     * @param processDescQueryDto
     * @return
     */
    IPage<ProcessDescVo> getByPage(Page<Void> page, @Param("params") ProcessDescQueryDto processDescQueryDto);
}
