package com.hete.supply.scm.server.scm.settle.dao;

import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPurchasePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 扣款单采购明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-05
 */
@Mapper
interface DeductOrderPurchaseMapper extends BaseDataMapper<DeductOrderPurchasePo> {

    List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(@Param("businessNoList") List<String> businessNoList,
                                                                   @Param("statusList") List<DeductStatus> statusList);

}
