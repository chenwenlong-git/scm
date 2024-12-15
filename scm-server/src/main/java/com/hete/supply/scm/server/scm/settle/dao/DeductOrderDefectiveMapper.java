package com.hete.supply.scm.server.scm.settle.dao;

import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderDefectivePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 扣款单表次品退供明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-06-21
 */
@Mapper
interface DeductOrderDefectiveMapper extends BaseDataMapper<DeductOrderDefectivePo> {

    List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(@Param("businessNoList") List<String> businessNoList,
                                                                   @Param("statusList") List<DeductStatus> statusList);
}
