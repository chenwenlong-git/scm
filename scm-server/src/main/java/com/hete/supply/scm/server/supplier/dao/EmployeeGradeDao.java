package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.supplier.converter.SupplierEmployeeConverter;
import com.hete.supply.scm.server.supplier.entity.dto.EmployeeGradeSearchDto;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradePo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeGradeSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 员工职级表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Component
@Validated
public class EmployeeGradeDao extends BaseDao<EmployeeGradeMapper, EmployeeGradePo> {

    public CommonPageResult.PageInfo<EmployeeGradeSearchVo> searchEmployeeGrade(Page<EmployeeGradePo> page, EmployeeGradeSearchDto dto) {

        final LambdaQueryWrapper<EmployeeGradePo> wrapper = this.getWrapper(dto);
        final Page<EmployeeGradePo> pageResult = baseMapper.selectPage(page, wrapper);
        final List<EmployeeGradePo> records = pageResult.getRecords();
        List<EmployeeGradeSearchVo> employeeGradeSearchVoList = SupplierEmployeeConverter.employeeGradePoToSearchVo(records);

        return PageInfoUtil.getPageInfo(pageResult, employeeGradeSearchVoList);
    }

    private LambdaQueryWrapper<EmployeeGradePo> getWrapper(EmployeeGradeSearchDto dto) {
        return Wrappers.<EmployeeGradePo>lambdaQuery()
                .eq(null != dto.getGradeType(),
                        EmployeeGradePo::getGradeType, dto.getGradeType())
                .orderByDesc(EmployeeGradePo::getCreateTime);
    }

    public List<EmployeeGradePo> getByIds(Set<Long> gradeIds) {
        return baseMapper.selectBatchIds(gradeIds);
    }

    public EmployeeGradePo getByName(String gradeName) {
        if (StringUtils.isBlank(gradeName)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<EmployeeGradePo>lambdaQuery()
                .eq(EmployeeGradePo::getGradeName, gradeName));
    }
}
