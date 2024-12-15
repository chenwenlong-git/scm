package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.entity.bo.ProcessCodeMappingEntryBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工序管理表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessMapper extends BaseDataMapper<ProcessPo> {

    /**
     * 分页查询推送消息
     *
     * @param page
     * @param processQueryDto
     * @return
     */
    IPage<ProcessVo> getByPage(Page<Void> page, @Param("params") ProcessQueryDto processQueryDto);

    List<ProcessCodeMappingEntryBo> getProcessCodeMapping();
}
