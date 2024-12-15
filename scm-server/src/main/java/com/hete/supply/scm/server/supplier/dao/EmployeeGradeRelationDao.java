package com.hete.supply.scm.server.supplier.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import com.hete.supply.scm.server.scm.process.converter.EmployeeConverter;
import com.hete.supply.scm.server.scm.process.entity.bo.GradeTypeCountBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessEmployeeQueryRequestDto;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessEmployeeVo;
import com.hete.supply.scm.server.supplier.entity.dto.EmployeeSearchDto;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeRelationPo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 员工职级关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Component
@Validated
public class EmployeeGradeRelationDao extends BaseDao<EmployeeGradeRelationMapper, EmployeeGradeRelationPo> {

    public CommonPageResult.PageInfo<EmployeeSearchVo> searchEmployee(Page<Void> page,
                                                                      EmployeeSearchDto dto) {
        final IPage<EmployeeSearchVo> pageResult = baseMapper.searchEmployee(page, dto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<EmployeeGradeRelationPo> getByEmployees(Set<String> employeeNos) {
        return list(Wrappers.<EmployeeGradeRelationPo>lambdaQuery()
                .in(EmployeeGradeRelationPo::getEmployeeNo, employeeNos));
    }

    public List<EmployeeGradeRelationPo> getAll() {
        return baseMapper.selectList(null);
    }

    public void removeByEmployeeNoList(List<String> employeeNoList) {
        if (CollectionUtils.isEmpty(employeeNoList)) {
            return;
        }

        baseMapper.delete(Wrappers.<EmployeeGradeRelationPo>lambdaUpdate()
                .in(EmployeeGradeRelationPo::getEmployeeNo, employeeNoList));
    }

    public CommonPageResult.PageInfo<ProcessEmployeeVo> getByPage(ProcessEmployeeQueryRequestDto request) {
        Page<EmployeeGradeRelationPo> page = new Page<>(request.getPageNo(), request.getPageSize());
        LambdaQueryWrapper<EmployeeGradeRelationPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(request.getEmployeeName()), EmployeeGradeRelationPo::getEmployeeName,
                request.getEmployeeName());
        queryWrapper.eq(StrUtil.isNotBlank(request.getEmployeeNo()), EmployeeGradeRelationPo::getEmployeeNo,
                request.getEmployeeNo());
        Page<EmployeeGradeRelationPo> employeeGradeRelationPoPage = baseMapper.selectPage(page, queryWrapper);
        return PageInfoUtil.getPageInfo(EmployeeConverter.convertToProcessEmployeeVoPage(employeeGradeRelationPoPage));
    }

    public List<GradeTypeCountBo> countDistinctEmployeesByGradeType(List<GradeType> gradeTypes) {
        return baseMapper.countDistinctEmployeesByGradeType(gradeTypes);
    }


}















