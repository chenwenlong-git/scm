package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import com.hete.supply.scm.server.scm.process.entity.bo.GradeTypeCountBo;
import com.hete.supply.scm.server.supplier.entity.dto.EmployeeSearchDto;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeRelationPo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 员工职级关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Mapper
interface EmployeeGradeRelationMapper extends BaseDataMapper<EmployeeGradeRelationPo> {

    IPage<EmployeeSearchVo> searchEmployee(Page<Void> page, @Param("dto") EmployeeSearchDto dto);

    List<GradeTypeCountBo> countDistinctEmployeesByGradeType(List<GradeType> gradeTypes);
}
