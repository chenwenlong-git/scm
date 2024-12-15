package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.entity.bo.ProcessCodeMappingEntryBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessQueryDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessQueryListDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 工序管理表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessDao extends BaseDao<ProcessMapper, ProcessPo> {


    /**
     * 分页查询
     *
     * @param page
     * @param processQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessVo> getByPage(Page<Void> page, ProcessQueryDto processQueryDto) {
        IPage<ProcessVo> pageResult = baseMapper.getByPage(page, processQueryDto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 获取最新的工序
     *
     * @return
     */
    public ProcessPo getLatest(ProcessFirst processFirst) {
        return getOne(Wrappers.<ProcessPo>lambdaQuery()
                .eq(ProcessPo::getProcessFirst, processFirst)
                .orderByDesc(ProcessPo::getProcessId)
                .last("limit 1"));
    }

    /**
     * 通过一级工序以及二级工序名称查询
     *
     * @param processFirst
     * @param procedureSecondName
     * @return
     */
    public ProcessPo getByProcedureFirstAndSecondName(ProcessFirst processFirst, String procedureSecondName, ProcessLabel processLabel) {
        return getOne(Wrappers.<ProcessPo>lambdaQuery()
                .eq(ProcessPo::getProcessFirst, processFirst)
                .eq(ProcessPo::getProcessSecondName, procedureSecondName)
                .eq(ProcessPo::getProcessLabel, processLabel));

    }

    /**
     * 通过工序名称查询
     *
     * @param processNames
     * @return
     */
    public List<ProcessPo> getByProcessNames(List<String> processNames) {
        return list(Wrappers.<ProcessPo>lambdaQuery()
                .in(ProcessPo::getProcessName, processNames));

    }

    /**
     * 获取所有的列表
     *
     * @return
     */
    public List<ProcessPo> getAll() {
        return list(Wrappers.<ProcessPo>lambdaQuery().eq(ProcessPo::getProcessStatus, ProcessStatus.ENABLED));
    }

    /**
     * 根据工序ID集合查询工序信息列表
     *
     * @param processIds 工序ID集合
     * @return 工序信息列表，如果工序ID集合为空或未查询到数据，返回空列表
     */
    public List<ProcessPo> getByProcessIds(Collection<Long> processIds) {
        if (CollectionUtils.isEmpty(processIds)) {
            return Collections.emptyList();
        }

        // 使用MyBatis Plus的lambdaQuery进行查询，根据工序ID集合进行in条件查询
        return list(Wrappers.<ProcessPo>lambdaQuery().in(ProcessPo::getProcessId, processIds));
    }


    /**
     * 通过多个 code 查询工序
     *
     * @param processCodes
     * @return
     */
    public List<ProcessPo> getByProcessCodes(Collection<String> processCodes) {
        if (CollectionUtils.isEmpty(processCodes)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessPo>lambdaQuery()
                .in(ProcessPo::getProcessCode, processCodes));
    }

    /**
     * 通过编号获取工序
     *
     * @param processCode
     * @return
     */
    public ProcessPo getByProcessCode(String processCode) {
        return getOne(Wrappers.<ProcessPo>lambdaQuery()
                .eq(ProcessPo::getProcessCode, processCode));
    }

    /**
     * 通过条件查询列表
     *
     * @author ChenWenLong
     * @date 2023/3/13 17:40
     */
    public List<ProcessPo> getByList(ProcessQueryListDto dto) {
        return list(Wrappers.<ProcessPo>lambdaQuery()
                .eq(dto.getProcessLabel() != null, ProcessPo::getProcessLabel, dto.getProcessLabel()));

    }

    public ProcessPo getOneByProcessSecondNameAndLabel(String processSecondName, ProcessLabel processLabel) {
        if (StringUtils.isBlank(processSecondName) || null == processLabel) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<ProcessPo>lambdaQuery()
                .eq(ProcessPo::getProcessSecondName, processSecondName)
                .eq(ProcessPo::getProcessLabel, processLabel));
    }

    public List<ProcessCodeMappingEntryBo> getProcessCodeMapping() {
        return baseMapper.getProcessCodeMapping();
    }

    public List<ProcessPo> getProcessesWithExtraCommissionGreaterThan(BigDecimal bigDecimal) {
        LambdaQueryWrapper<ProcessPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(ProcessPo::getExtraCommission, bigDecimal);
        return baseMapper.selectList(queryWrapper);
    }
}
