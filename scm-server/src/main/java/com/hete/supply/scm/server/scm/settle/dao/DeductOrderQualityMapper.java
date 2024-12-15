package com.hete.supply.scm.server.scm.settle.dao;

import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderQualityPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 扣款单品质扣款明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-14
 */
@Mapper
interface DeductOrderQualityMapper extends BaseDataMapper<DeductOrderQualityPo> {

    List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(@Param("businessNoList") List<String> businessNoList,
                                                                   @Param("statusList") List<DeductStatus> statusList);
}
